package com.on_class.bootcamp.domain.usecase;


import com.on_class.bootcamp.domain.api.IBootcampServicePort;
import com.on_class.bootcamp.domain.constants.DomainConstants;
import com.on_class.bootcamp.domain.enums.TechnicalMessage;
import com.on_class.bootcamp.domain.exceptions.BusinessException;
import com.on_class.bootcamp.domain.model.Bootcamp;
import com.on_class.bootcamp.domain.model.Capability;
import com.on_class.bootcamp.domain.model.PaginationAndFilter;
import com.on_class.bootcamp.domain.model.PaginationResponse;
import com.on_class.bootcamp.domain.spi.IBootcampPersistencePort;
import com.on_class.bootcamp.domain.spi.ICapabilityExternalPort;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BootcampUseCase implements IBootcampServicePort {

    private final IBootcampPersistencePort bootcampPersistencePort;
    private final ICapabilityExternalPort  capabilityExternalPort;

    public BootcampUseCase(IBootcampPersistencePort bootcampPersistencePort, ICapabilityExternalPort capabilityExternalPort) {
        this.bootcampPersistencePort = bootcampPersistencePort;
        this.capabilityExternalPort = capabilityExternalPort;
    }

    @Override
    public Mono<Void> createBootcamp(Bootcamp bootcamp) {
        return validateCapabilities(bootcamp)
                .flatMap(validatedBootcamp -> bootcampPersistencePort.findByName(validatedBootcamp.getName()))
                .flatMap(existing -> Mono.error(new BusinessException(TechnicalMessage.ALREADY_EXISTS)))
                .switchIfEmpty(
                        Mono.defer(() ->
                                bootcampPersistencePort.createBootcamp(bootcamp)
                                        .flatMap(createdBootcamp ->
                                                capabilityExternalPort.linkCapabilitiesToBootcamp(
                                                                createdBootcamp.getId(), bootcamp.getCapabilities()
                                                        )
                                                        .onErrorResume(ex -> rollbackBootcampCreation(createdBootcamp.getId(), ex))
                                        )
                        )
                )
                .then();
    }

    @Override
    public Mono<PaginationResponse<Bootcamp>> getPaginatedBootcamps(PaginationAndFilter paginationAndFilter) {
        return validatePaginationAndFilter(paginationAndFilter)
                .flatMap(validated ->
                        bootcampPersistencePort.getBootcamps(paginationAndFilter))
                .flatMap(this::enrichBootcampsWithCapabilities);
    }

    private Mono<PaginationResponse<Bootcamp>> enrichBootcampsWithCapabilities(PaginationResponse<Bootcamp> paginatedBootcamps) {
        List<Long> bootcampIds = paginatedBootcamps.getElements()
                .stream()
                .map(Bootcamp::getId)
                .toList();

        if (bootcampIds.isEmpty()) {
            return Mono.just(paginatedBootcamps);
        }

        return capabilityExternalPort.getBootcampsCapabilities(bootcampIds)
                .collectList()
                .map(bootcampsCapabilities -> buildNewPaginationResponse(paginatedBootcamps, bootcampsCapabilities));

    }

    private PaginationResponse<Bootcamp> buildNewPaginationResponse(PaginationResponse<Bootcamp> paginatedBootcamps,
                                                                      List<Bootcamp> bootcampsCapabilities) {
        Map<Long, Bootcamp> bootcampsCapabilitiesMap = bootcampsCapabilities.stream()
                .collect(Collectors.toMap(Bootcamp::getId, Function.identity()));

        List<Bootcamp> newBootcamps = paginatedBootcamps.getElements().stream()
                .map(bootcamp -> {
                    List<Capability> capabilities = Optional.ofNullable(bootcampsCapabilitiesMap.get(bootcamp.getId()))
                            .map(Bootcamp::getCapabilities)
                            .orElse(Collections.emptyList());

                    return new Bootcamp(
                            bootcamp.getId(),
                            bootcamp.getName(),
                            bootcamp.getDescription(),
                            bootcamp.getLaunchDate(),
                            bootcamp.getDuration(),
                            capabilities
                    );
                })
                .toList();

        return new PaginationResponse<>(
                paginatedBootcamps.getTotalPages(),
                paginatedBootcamps.getCurrentPage(),
                paginatedBootcamps.getTotalElements(),
                newBootcamps
        );
    }

    private Mono<Void> rollbackBootcampCreation(Long id, Throwable ex) {
        return bootcampPersistencePort.deleteBootcampById(id)
                .then(Mono.error(ex));
    }

    private Mono<Bootcamp> validateCapabilities(Bootcamp bootcamp) {

        List<Capability> capabilities = bootcamp.getCapabilities();

        if (capabilities.size() < DomainConstants.MIN_CAPABILITIES || capabilities.size() > DomainConstants.MAX_CAPABILITIES) {
            return Mono.error(new BusinessException(TechnicalMessage.INVALID_REQUEST,List.of(DomainConstants.EXCEPTION_CAPABILITIES_INVALID_QUANTITY)));
        }

        long uniqueIdsCount = capabilities.stream()
                .map(Capability::getId)
                .distinct()
                .count();

        if (uniqueIdsCount != capabilities.size()) {
            return Mono.error(new BusinessException(TechnicalMessage.INVALID_REQUEST,List.of(DomainConstants.EXCEPTION_CAPABILITIES_DUPLICATED_ID)));
        }
        return Mono.just(bootcamp);
    }

    private Mono<PaginationAndFilter> validatePaginationAndFilter(PaginationAndFilter paginationAndFilter) {

        List<String> errors = new ArrayList<>();

        if (paginationAndFilter.getSize() <= DomainConstants.PAGINATION_MIN_SIZE) {
            errors.add(DomainConstants.EXCEPTION_PAGINATION_SIZE);
        }

        if (paginationAndFilter.getPage() < DomainConstants.PAGINATION_MIN_PAGE) {
            errors.add(DomainConstants.EXCEPTION_PAGINATION_PAGE);
        }

        Set<String> validSortDirections = Set.of(DomainConstants.SORT_BY_ASC, DomainConstants.SORT_BY_DESC);

        if (!validSortDirections.contains(paginationAndFilter.getSortDirection().toUpperCase())) {
            errors.add(DomainConstants.EXCEPTION_PAGINATION_SORT);
        }

        Set<String> validSortFields = Set.of(DomainConstants.BOOTCAMP_SORT_BY_NAME, DomainConstants.BOOTCAMP_SORT_BY_CAPABILITY_QUANTITY);

        if (!validSortFields.contains(paginationAndFilter.getSortField().toLowerCase())) {
            errors.add(DomainConstants.EXCEPTION_PAGINATION_SORT_FIELD);
        }

        if (!errors.isEmpty()) {
            return Mono.error(new BusinessException(TechnicalMessage.PAGINATION_BAD_REQUEST,errors));
        }

        return Mono.just(paginationAndFilter);
    }
}
