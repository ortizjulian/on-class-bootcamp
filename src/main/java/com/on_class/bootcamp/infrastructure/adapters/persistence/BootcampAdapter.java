package com.on_class.bootcamp.infrastructure.adapters.persistence;

import com.on_class.bootcamp.domain.model.Bootcamp;
import com.on_class.bootcamp.domain.spi.IBootcampPersistencePort;
import com.on_class.bootcamp.infrastructure.adapters.persistence.mapper.IBootcampEntityMapper;
import com.on_class.bootcamp.infrastructure.adapters.persistence.repository.IBootcampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BootcampAdapter implements IBootcampPersistencePort {

    private final IBootcampRepository bootcampRepository;
    private final IBootcampEntityMapper bootcampEntityMapper;


    @Override
    public Mono<Bootcamp> createBootcamp(Bootcamp bootcamp) {
        return bootcampRepository.save(bootcampEntityMapper.toBootcampEntity(bootcamp))
                .map(bootcampEntityMapper::toBootcamp);
    }

    @Override
    public Mono<Void> deleteBootcampById(Long id) {
        return bootcampRepository.findById(id)
                .flatMap(bootcampRepository::delete);
    }

    @Override
    public Mono<Bootcamp> findByName(String name) {
        return bootcampRepository.findByName(name)
                .map(bootcampEntityMapper::toBootcamp);
    }
}
