package com.on_class.bootcamp.infrastructure.adapters.persistence;

import com.on_class.bootcamp.domain.model.Bootcamp;
import com.on_class.bootcamp.domain.model.PaginationAndFilter;
import com.on_class.bootcamp.domain.model.PaginationResponse;
import com.on_class.bootcamp.domain.spi.IBootcampPersistencePort;
import com.on_class.bootcamp.infrastructure.adapters.persistence.mapper.IBootcampEntityMapper;
import com.on_class.bootcamp.infrastructure.adapters.persistence.repository.IBootcampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Override
    public Mono<PaginationResponse<Bootcamp>> getBootcamps(PaginationAndFilter paginationAndFilter) {
        Pageable pageable = PageRequest.of(
                paginationAndFilter.getPage(),
                paginationAndFilter.getSize(),
                Sort.by(Sort.Direction.fromString(paginationAndFilter.getSortDirection()),
                        paginationAndFilter.getSortField())
        );

        return bootcampRepository.findAllBy(pageable)
                .collectList()
                .zipWith(bootcampRepository.count())
                .map(zip -> {
                    List<Bootcamp> bootcamps = bootcampEntityMapper.toBootcamps(zip.getT1());

                    long totalElements = zip.getT2();
                    int size = pageable.getPageSize();
                    int currentPage = pageable.getPageNumber();
                    int totalPages = (int) ((totalElements + size - 1) / size);

                    return new PaginationResponse<>(totalPages,currentPage,totalElements,bootcamps);
                });
    }
}
