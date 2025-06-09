package com.on_class.bootcamp.infrastructure.adapters.webclient.mapper;


import com.on_class.bootcamp.domain.model.Capability;
import com.on_class.bootcamp.infrastructure.adapters.webclient.dto.PostBootcampCapabilitiesRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBootcampCapabilitiesMapper {

    default PostBootcampCapabilitiesRequestDto toBootcampCapabilitiesRequestDto(List<Capability> capabilities) {
        return PostBootcampCapabilitiesRequestDto
                .builder()
                .capabilityIds(capabilities.stream().map(Capability::getId).toList())
                .build();
    }
}
