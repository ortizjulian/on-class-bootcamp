package com.on_class.bootcamp.infrastructure.entrypoints.util;

import com.on_class.bootcamp.domain.enums.TechnicalMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ErrorResponseBuilder {

    public Mono<ServerResponse> buildErrorResponse(TechnicalMessage error) {
        return Mono.defer(() -> {
            ErrorDto errorDto = ErrorDto.builder()
                    .code(error.getCode())
                    .message(error.getMessage())
                    .build();
            return ServerResponse.status(Integer.parseInt(error.getCode()))
                    .bodyValue(errorDto);
        });
    }

    public Mono<ServerResponse> buildErrorResponse(TechnicalMessage error, List<String> details) {
        return Mono.defer(() -> {
            ErrorDto errorDto = ErrorDto.builder()
                    .code(error.getCode())
                    .message(error.getMessage())
                    .details(details)
                    .build();
            return ServerResponse.status(Integer.parseInt(error.getCode()))
                    .bodyValue(errorDto);
        });
    }
}

