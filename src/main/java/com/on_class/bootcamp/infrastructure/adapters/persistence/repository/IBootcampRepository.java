package com.on_class.bootcamp.infrastructure.adapters.persistence.repository;

import com.on_class.bootcamp.infrastructure.adapters.persistence.entity.BootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IBootcampRepository extends ReactiveCrudRepository<BootcampEntity, Long> {
    Mono<BootcampEntity> findByName(String name);
}
