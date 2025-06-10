package com.on_class.bootcamp.infrastructure.adapters.persistence.mapper;

import com.on_class.bootcamp.domain.model.Bootcamp;
import com.on_class.bootcamp.infrastructure.adapters.persistence.entity.BootcampEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBootcampEntityMapper {
    default BootcampEntity toBootcampEntity(Bootcamp bootcamp) {
        return BootcampEntity.builder()
                .name(bootcamp.getName())
                .description(bootcamp.getDescription())
                .duration(bootcamp.getDuration())
                .launchDate(bootcamp.getLaunchDate())
                .capabilityQuantity(bootcamp.getCapabilities().size())
                .build();
    }

    Bootcamp toBootcamp(BootcampEntity bootcampEntity);
    List<Bootcamp> toBootcamps(List<BootcampEntity> bootcampEntityList);
}

