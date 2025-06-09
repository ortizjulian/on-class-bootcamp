package com.on_class.bootcamp.domain.api;

import com.on_class.bootcamp.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface IBootcampServicePort {
    Mono<Void> createBootcamp(Bootcamp bootcamp);
}
