package com.on_class.bootcamp.infrastructure.adapters.webclient;

import com.on_class.bootcamp.domain.enums.TechnicalMessage;
import com.on_class.bootcamp.domain.exceptions.BusinessException;
import com.on_class.bootcamp.domain.exceptions.TechnicalException;
import com.on_class.bootcamp.domain.model.Bootcamp;
import com.on_class.bootcamp.domain.model.Capability;
import com.on_class.bootcamp.domain.spi.ICapabilityExternalPort;

import com.on_class.bootcamp.infrastructure.adapters.webclient.dto.BootcampResponseDto;
import com.on_class.bootcamp.infrastructure.adapters.webclient.dto.ErrorResponse;
import com.on_class.bootcamp.infrastructure.adapters.webclient.dto.GetBootcampCapabilitiesRequestDto;
import com.on_class.bootcamp.infrastructure.adapters.webclient.mapper.IBootcampCapabilitiesMapper;
import com.on_class.bootcamp.infrastructure.adapters.webclient.mapper.IBootcampRequestDtoMapper;
import com.on_class.bootcamp.infrastructure.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CapabilityAdapter implements ICapabilityExternalPort {

    private final WebClient webClient;
    private final IBootcampCapabilitiesMapper bootcampCapabilitiesMapper;
    private final IBootcampRequestDtoMapper bootcampRequestDtoMapper;

    @Override
    public Mono<Void> linkCapabilitiesToBootcamp(Long idBootcamp, List<Capability> capabilities) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(Constants.BOOTCAMP_CAPABILITIES_PATH)
                        .build(idBootcamp))
                .bodyValue(bootcampCapabilitiesMapper.toBootcampCapabilitiesRequestDto(capabilities))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> buildErrorResponse(response, TechnicalMessage.ADAPTER_RESPONSE_NOT_FOUND))
                .onStatus(HttpStatusCode::is5xxServerError, response -> buildErrorResponse(response, TechnicalMessage.INTERNAL_ERROR_IN_ADAPTERS))
                .toBodilessEntity()
                .then();
    }

    @Override
    public Flux<Bootcamp> getBootcampsCapabilities(List<Long> bootcampIds) {
        return webClient.post()
                .uri(Constants.BOOTCAMP_BY_IDS_PATH)
                .bodyValue(new GetBootcampCapabilitiesRequestDto(bootcampIds))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> buildErrorResponse(response, TechnicalMessage.ADAPTER_RESPONSE_NOT_FOUND))
                .onStatus(HttpStatusCode::is5xxServerError, response -> buildErrorResponse(response, TechnicalMessage.INTERNAL_ERROR_IN_ADAPTERS))
                .bodyToFlux(BootcampResponseDto.class)
                .map(bootcampRequestDtoMapper::toBootcamp);
    }

    private Mono<Throwable> buildErrorResponse(ClientResponse response, TechnicalMessage technicalMessage) {
        return response.bodyToMono(ErrorResponse.class)
                .defaultIfEmpty(ErrorResponse.builder().message(Constants.NO_ADDITIONAL_DETAILS).build())
                .flatMap(errorBody -> Mono.error(
                            response.statusCode().is5xxServerError() ?
                                    new TechnicalException(technicalMessage):
                                    new BusinessException(technicalMessage,List.of(errorBody.getMessage())))
                );
    }

}
