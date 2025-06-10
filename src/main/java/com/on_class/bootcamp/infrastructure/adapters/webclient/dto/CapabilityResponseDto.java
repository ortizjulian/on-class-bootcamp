package com.on_class.bootcamp.infrastructure.adapters.webclient.dto;

import lombok.Data;

import java.util.List;

@Data
public class CapabilityResponseDto {
    private Long id;
    private String name;
    private List<TechnologyResponseDto> technologies;
}
