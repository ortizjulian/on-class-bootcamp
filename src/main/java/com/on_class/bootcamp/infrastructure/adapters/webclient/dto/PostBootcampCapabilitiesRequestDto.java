package com.on_class.bootcamp.infrastructure.adapters.webclient.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostBootcampCapabilitiesRequestDto {
    private List<Long> capabilityIds;
}
