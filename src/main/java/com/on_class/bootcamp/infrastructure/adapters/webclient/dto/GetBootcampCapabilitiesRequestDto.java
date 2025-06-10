package com.on_class.bootcamp.infrastructure.adapters.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetBootcampCapabilitiesRequestDto {
    private List<Long> bootcampIds;
}
