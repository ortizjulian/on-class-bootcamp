package com.on_class.bootcamp.infrastructure.adapters.webclient.dto;

import lombok.Data;

import java.util.List;

@Data
public class BootcampResponseDto {
    private Long id;
    private List<CapabilityResponseDto> capabilities;
}
