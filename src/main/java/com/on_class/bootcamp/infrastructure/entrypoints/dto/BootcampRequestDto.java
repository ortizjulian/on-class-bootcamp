package com.on_class.bootcamp.infrastructure.entrypoints.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BootcampRequestDto {

    private String name;
    private String description;
    private LocalDate launchDate;
    private Integer duration;
    private List<Long> capabilityIds;
}
