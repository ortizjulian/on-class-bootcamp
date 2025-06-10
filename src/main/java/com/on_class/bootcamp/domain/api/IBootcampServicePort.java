package com.on_class.bootcamp.domain.api;

import com.on_class.bootcamp.domain.model.Bootcamp;
import com.on_class.bootcamp.domain.model.PaginationAndFilter;
import com.on_class.bootcamp.domain.model.PaginationResponse;
import reactor.core.publisher.Mono;

public interface IBootcampServicePort {
    Mono<Void> createBootcamp(Bootcamp bootcamp);
    Mono<PaginationResponse<Bootcamp>> getPaginatedBootcamps(PaginationAndFilter paginationAndFilter);
}
