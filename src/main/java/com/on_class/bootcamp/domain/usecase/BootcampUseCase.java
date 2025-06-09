package com.on_class.bootcamp.domain.usecase;


import com.on_class.bootcamp.domain.api.IBootcampServicePort;
import com.on_class.bootcamp.domain.constants.DomainConstants;
import com.on_class.bootcamp.domain.enums.TechnicalMessage;
import com.on_class.bootcamp.domain.exceptions.BusinessException;
import com.on_class.bootcamp.domain.model.Bootcamp;
import com.on_class.bootcamp.domain.model.Capability;
import com.on_class.bootcamp.domain.spi.IBootcampPersistencePort;
import com.on_class.bootcamp.domain.spi.ICapabilityExternalPort;
import reactor.core.publisher.Mono;

import java.util.List;

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
}
