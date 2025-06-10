package com.on_class.bootcamp.domain.usecase;

import com.on_class.bootcamp.domain.exceptions.BusinessException;
import com.on_class.bootcamp.domain.model.Bootcamp;
import com.on_class.bootcamp.domain.model.Capability;
import com.on_class.bootcamp.domain.model.PaginationAndFilter;
import com.on_class.bootcamp.domain.model.PaginationResponse;
import com.on_class.bootcamp.domain.spi.IBootcampPersistencePort;
import com.on_class.bootcamp.domain.spi.ICapabilityExternalPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BootcampUseCaseTest {

    @Mock
    private IBootcampPersistencePort bootcampPersistencePort;

    @Mock
    private ICapabilityExternalPort capabilityExternalPort;

    @InjectMocks
    private BootcampUseCase bootcampUseCase;

    @Test
    void createBootcamp_whenNameAlreadyExists_thenThrowsBusinessException() {
        List<Capability> capabilities = List.of(
                new Capability(1L),
                new Capability(2L),
                new Capability(3L)
        );
        
        Bootcamp bootcamp = new Bootcamp("Java Bootcamp", "Java training program", 
                LocalDate.now(), 12, capabilities);
        
        when(bootcampPersistencePort.findByName(bootcamp.getName()))
                .thenReturn(Mono.just(bootcamp));

        StepVerifier.create(bootcampUseCase.createBootcamp(bootcamp))
                .expectError(BusinessException.class)
                .verify();

        verify(bootcampPersistencePort).findByName("Java Bootcamp");
        verify(bootcampPersistencePort, never()).createBootcamp(any());
        verifyNoInteractions(capabilityExternalPort);
    }

    @Test
    void createBootcamp_whenInvalidCapabilitiesQuantity_thenThrowsBusinessException() {
        List<Capability> capabilities = List.of(
        );
        
        Bootcamp bootcamp = new Bootcamp("Java Bootcamp", "Java training program", 
                LocalDate.now(), 12, capabilities);

        StepVerifier.create(bootcampUseCase.createBootcamp(bootcamp))
                .expectError(BusinessException.class)
                .verify();

        verifyNoInteractions(bootcampPersistencePort, capabilityExternalPort);
    }

    @Test
    void createBootcamp_whenDuplicatedCapabilityIds_thenThrowsBusinessException() {
        // Arrange
        List<Capability> capabilities = List.of(
                new Capability(1L),
                new Capability(2L),
                new Capability(1L)
        );
        
        Bootcamp bootcamp = new Bootcamp("Java Bootcamp", "Java training program", 
                LocalDate.now(), 12, capabilities);

        StepVerifier.create(bootcampUseCase.createBootcamp(bootcamp))
                .expectError(BusinessException.class)
                .verify();

        verifyNoInteractions(bootcampPersistencePort, capabilityExternalPort);
    }

    @Test
    void createBootcamp_whenNameDoesNotExistsButLinkCapabilitiesFails_thenThrowsException() {
        List<Capability> capabilities = List.of(
                new Capability(1L),
                new Capability(2L),
                new Capability(3L)
        );
        
        Bootcamp bootcamp = new Bootcamp("Java Bootcamp", "Java training program", 
                LocalDate.now(), 12, capabilities);
        
        Bootcamp createdBootcamp = new Bootcamp("Java Bootcamp", "Java training program", 
                LocalDate.now(), 12, capabilities);
        createdBootcamp.setId(1L);

        when(bootcampPersistencePort.findByName("Java Bootcamp"))
                .thenReturn(Mono.empty());

        when(bootcampPersistencePort.createBootcamp(bootcamp))
                .thenReturn(Mono.just(createdBootcamp));

        when(capabilityExternalPort.linkCapabilitiesToBootcamp(1L, capabilities))
                .thenReturn(Mono.error(new RuntimeException("Linking failed")));

        when(bootcampPersistencePort.deleteBootcampById(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(bootcampUseCase.createBootcamp(bootcamp))
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().equals("Linking failed"))
                .verify();

        verify(bootcampPersistencePort).findByName("Java Bootcamp");
        verify(bootcampPersistencePort).createBootcamp(bootcamp);
        verify(capabilityExternalPort).linkCapabilitiesToBootcamp(1L, capabilities);
        verify(bootcampPersistencePort).deleteBootcampById(1L);
    }

    @Test
    void createBootcamp_whenEverythingIsValid_thenSuccess() {
        // Arrange
        List<Capability> capabilities = List.of(
                new Capability(1L),
                new Capability(2L),
                new Capability(3L)
        );
        
        Bootcamp bootcamp = new Bootcamp("Java Bootcamp", "Java training program", 
                LocalDate.now(), 12, capabilities);
        
        Bootcamp createdBootcamp = new Bootcamp("Java Bootcamp", "Java training program", 
                LocalDate.now(), 12, capabilities);
        createdBootcamp.setId(1L);

        when(bootcampPersistencePort.findByName("Java Bootcamp"))
                .thenReturn(Mono.empty());

        when(bootcampPersistencePort.createBootcamp(bootcamp))
                .thenReturn(Mono.just(createdBootcamp));

        when(capabilityExternalPort.linkCapabilitiesToBootcamp(1L, capabilities))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(bootcampUseCase.createBootcamp(bootcamp))
                .verifyComplete();

        verify(bootcampPersistencePort).findByName("Java Bootcamp");
        verify(bootcampPersistencePort).createBootcamp(bootcamp);
        verify(capabilityExternalPort).linkCapabilitiesToBootcamp(1L, capabilities);
    }
    
    @Test
    void getPaginatedBootcamps_whenInvalidPaginationSize_thenThrowsBusinessException() {
        // Arrange
        PaginationAndFilter paginationAndFilter = new PaginationAndFilter(0, 0, "ASC", "name");
        
        // Act & Assert
        StepVerifier.create(bootcampUseCase.getPaginatedBootcamps(paginationAndFilter))
                .expectError(BusinessException.class)
                .verify();
                
        verifyNoInteractions(bootcampPersistencePort, capabilityExternalPort);
    }
    
    @Test
    void getPaginatedBootcamps_whenInvalidPaginationPage_thenThrowsBusinessException() {
        // Arrange
        PaginationAndFilter paginationAndFilter = new PaginationAndFilter(-1, 10, "ASC", "name");
        
        // Act & Assert
        StepVerifier.create(bootcampUseCase.getPaginatedBootcamps(paginationAndFilter))
                .expectError(BusinessException.class)
                .verify();
                
        verifyNoInteractions(bootcampPersistencePort, capabilityExternalPort);
    }
    
    @Test
    void getPaginatedBootcamps_whenInvalidSortDirection_thenThrowsBusinessException() {
        // Arrange
        PaginationAndFilter paginationAndFilter = new PaginationAndFilter(0, 10, "INVALID", "name");
        
        // Act & Assert
        StepVerifier.create(bootcampUseCase.getPaginatedBootcamps(paginationAndFilter))
                .expectError(BusinessException.class)
                .verify();
                
        verifyNoInteractions(bootcampPersistencePort, capabilityExternalPort);
    }
    
    @Test
    void getPaginatedBootcamps_whenInvalidSortField_thenThrowsBusinessException() {
        // Arrange
        PaginationAndFilter paginationAndFilter = new PaginationAndFilter(0, 10, "ASC", "invalid");
        
        // Act & Assert
        StepVerifier.create(bootcampUseCase.getPaginatedBootcamps(paginationAndFilter))
                .expectError(BusinessException.class)
                .verify();
                
        verifyNoInteractions(bootcampPersistencePort, capabilityExternalPort);
    }
    
    @Test
    void getPaginatedBootcamps_whenEmptyBootcampList_thenReturnEmptyPaginationResponse() {
        // Arrange
        PaginationAndFilter paginationAndFilter = new PaginationAndFilter(0, 10, "ASC", "name");
        PaginationResponse<Bootcamp> emptyResponse = new PaginationResponse<>(0, 0, 0L, new ArrayList<>());
        
        when(bootcampPersistencePort.getBootcamps(paginationAndFilter))
                .thenReturn(Mono.just(emptyResponse));
        
        // Act & Assert
        StepVerifier.create(bootcampUseCase.getPaginatedBootcamps(paginationAndFilter))
                .consumeNextWith(response -> {
                    assertEquals(0, response.getTotalPages());
                    assertEquals(0, response.getCurrentPage());
                    assertEquals(0L, response.getTotalElements());
                    assertEquals(0, response.getElements().size());
                })
                .verifyComplete();
                
        verify(bootcampPersistencePort).getBootcamps(paginationAndFilter);
        verifyNoInteractions(capabilityExternalPort);
    }
    
    @Test
    void getPaginatedBootcamps_whenValidPagination_thenReturnEnrichedBootcamps() {
        // Arrange
        PaginationAndFilter paginationAndFilter = new PaginationAndFilter(0, 10, "ASC", "name");
        
        List<Bootcamp> bootcamps = List.of(
            createBootcamp(1L, "Java Bootcamp", new ArrayList<>()),
            createBootcamp(2L, "AWS Bootcamp", new ArrayList<>())
        );
        
        PaginationResponse<Bootcamp> paginationResponse = new PaginationResponse<>(1, 0, 2L, bootcamps);
        
        List<Bootcamp> bootcampsWithCapabilities = List.of(
            createBootcamp(1L, "Java Bootcamp", List.of(new Capability(1L), new Capability(2L))),
            createBootcamp(2L, "AWS Bootcamp", List.of(new Capability(3L), new Capability(4L)))
        );
        
        when(bootcampPersistencePort.getBootcamps(paginationAndFilter))
                .thenReturn(Mono.just(paginationResponse));
                
        when(capabilityExternalPort.getBootcampsCapabilities(List.of(1L, 2L)))
                .thenReturn(Flux.fromIterable(bootcampsWithCapabilities));
        
        // Act & Assert
        StepVerifier.create(bootcampUseCase.getPaginatedBootcamps(paginationAndFilter))
                .consumeNextWith(response -> {
                    assertEquals(1, response.getTotalPages());
                    assertEquals(0, response.getCurrentPage());
                    assertEquals(2L, response.getTotalElements());
                    assertEquals(2, response.getElements().size());
                    assertEquals(2, response.getElements().get(0).getCapabilities().size());
                    assertEquals(2, response.getElements().get(1).getCapabilities().size());
                })
                .verifyComplete();
                
        verify(bootcampPersistencePort).getBootcamps(paginationAndFilter);
        verify(capabilityExternalPort).getBootcampsCapabilities(List.of(1L, 2L));
    }
    
    private Bootcamp createBootcamp(Long id, String name, List<Capability> capabilities) {
        Bootcamp bootcamp = new Bootcamp(name, "Description", LocalDate.now(), 12, capabilities);
        bootcamp.setId(id);
        return bootcamp;
    }
}