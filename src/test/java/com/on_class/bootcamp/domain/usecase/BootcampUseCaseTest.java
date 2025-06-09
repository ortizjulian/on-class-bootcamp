package com.on_class.bootcamp.domain.usecase;

import com.on_class.bootcamp.domain.exceptions.BusinessException;
import com.on_class.bootcamp.domain.model.Bootcamp;
import com.on_class.bootcamp.domain.model.Capability;
import com.on_class.bootcamp.domain.spi.IBootcampPersistencePort;
import com.on_class.bootcamp.domain.spi.ICapabilityExternalPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

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
}