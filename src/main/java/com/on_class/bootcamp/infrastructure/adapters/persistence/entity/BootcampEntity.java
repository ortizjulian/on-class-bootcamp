package com.on_class.bootcamp.infrastructure.adapters.persistence.entity;

import com.on_class.bootcamp.infrastructure.utils.Constants;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table(Constants.BOOTCAMP_TABLE_NAME)
@Getter
@Setter
@Builder
public class BootcampEntity {

    @Id
    private Long id;
    private String name;
    private String description;
    private LocalDate launchDate;
    private Integer duration;
    private Integer capabilityQuantity;
}
