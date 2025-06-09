package com.on_class.bootcamp.infrastructure.entrypoints.handler;


import com.on_class.bootcamp.domain.api.IBootcampServicePort;
import com.on_class.bootcamp.domain.enums.TechnicalMessage;
import com.on_class.bootcamp.domain.exceptions.BusinessException;
import com.on_class.bootcamp.domain.exceptions.TechnicalException;
import com.on_class.bootcamp.infrastructure.entrypoints.dto.BootcampRequestDto;
import com.on_class.bootcamp.infrastructure.entrypoints.mapper.IBootcampMapper;
import com.on_class.bootcamp.infrastructure.entrypoints.util.ErrorResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.on_class.bootcamp.infrastructure.utils.Constants.BOOTCAMP_ERROR;


@Component
@RequiredArgsConstructor
@Slf4j
public class BootcampHandler {

    private final IBootcampServicePort bootcampServicePort;
    private final IBootcampMapper bootcampMapper;
    private final ErrorResponseBuilder responseBuilder;

    public Mono<ServerResponse> createBootcamp(ServerRequest request) {
        return request.bodyToMono(BootcampRequestDto.class)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.EMPTY_BODY)))
                .map(bootcampMapper::toBootcamp)
                .flatMap(bootcampServicePort::createBootcamp)
                .flatMap(bootcamp -> ServerResponse.status(HttpStatus.CREATED).build())
                .then(ServerResponse.status(HttpStatus.CREATED).build())
                .doOnError(ex -> log.error(BOOTCAMP_ERROR, ex))
                .onErrorResume(BusinessException.class , ex ->  responseBuilder.buildErrorResponse(
                        ex.getTechnicalMessage(),
                        ex.getDetails()
                ))
                .onErrorResume(TechnicalException.class, ex ->  responseBuilder.buildErrorResponse(
                        ex.getTechnicalMessage()
                ))
                .onErrorResume(ex ->  responseBuilder.buildErrorResponse(
                        TechnicalMessage.INTERNAL_ERROR
                ));
    }
}
