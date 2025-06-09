package com.on_class.bootcamp.infrastructure.entrypoints.mapper;

import com.on_class.bootcamp.domain.model.Bootcamp;
import com.on_class.bootcamp.domain.model.Capability;
import com.on_class.bootcamp.infrastructure.entrypoints.dto.BootcampRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBootcampMapper {
    default Bootcamp toBootcamp(BootcampRequestDto bootcampRequestDto) {
        List<Capability> capabilities =
                bootcampRequestDto.getCapabilityIds().stream()
                        .map(Capability::new).toList();
        return new Bootcamp(
                bootcampRequestDto.getName(),
                bootcampRequestDto.getDescription(),
                bootcampRequestDto.getLaunchDate(),
                bootcampRequestDto.getDuration(),
                capabilities
        );
    }
}
