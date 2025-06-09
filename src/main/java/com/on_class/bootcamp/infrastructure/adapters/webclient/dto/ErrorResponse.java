package com.on_class.bootcamp.infrastructure.adapters.webclient.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private int code;
    private String message;
}
