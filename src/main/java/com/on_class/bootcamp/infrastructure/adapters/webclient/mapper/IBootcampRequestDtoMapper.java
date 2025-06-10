package com.on_class.bootcamp.infrastructure.adapters.webclient.mapper;

import com.on_class.bootcamp.domain.model.Bootcamp;
import com.on_class.bootcamp.infrastructure.adapters.webclient.dto.BootcampResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBootcampRequestDtoMapper {
    Bootcamp toBootcamp(BootcampResponseDto bootcampResponseDto);
}
