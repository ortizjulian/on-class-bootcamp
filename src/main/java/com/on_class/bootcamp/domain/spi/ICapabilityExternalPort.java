package com.on_class.bootcamp.domain.spi;

import com.on_class.bootcamp.domain.model.Bootcamp;
import com.on_class.bootcamp.domain.model.Capability;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapabilityExternalPort {
    Mono<Void> linkCapabilitiesToBootcamp(Long idBootcamp, List<Capability> capabilities);
    Flux<Bootcamp> getBootcampsCapabilities(List<Long> bootcampIds);
}
