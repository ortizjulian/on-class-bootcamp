package com.on_class.bootcamp.domain.spi;

import com.on_class.bootcamp.domain.model.Bootcamp;
import com.on_class.bootcamp.domain.model.PaginationAndFilter;
import com.on_class.bootcamp.domain.model.PaginationResponse;
import reactor.core.publisher.Mono;

public interface IBootcampPersistencePort {
    Mono<Bootcamp> createBootcamp(Bootcamp bootcamp);
    Mono<Void> deleteBootcampById(Long id);
    Mono<Bootcamp> findByName(String name);
    Mono<PaginationResponse<Bootcamp>> getBootcamps(PaginationAndFilter paginationAndFilter);
}
