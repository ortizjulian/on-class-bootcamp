package com.on_class.bootcamp.application;

import com.on_class.bootcamp.domain.api.IBootcampServicePort;
import com.on_class.bootcamp.domain.spi.IBootcampPersistencePort;
import com.on_class.bootcamp.domain.spi.ICapabilityExternalPort;
import com.on_class.bootcamp.domain.usecase.BootcampUseCase;
import com.on_class.bootcamp.infrastructure.adapters.webclient.CapabilityAdapter;
import com.on_class.bootcamp.infrastructure.adapters.webclient.mapper.IBootcampCapabilitiesMapper;
import com.on_class.bootcamp.infrastructure.adapters.webclient.mapper.IBootcampRequestDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final CapabilityProperties capabilityProperties;
    private final IBootcampPersistencePort bootcampPersistencePort;
    private final IBootcampCapabilitiesMapper bootcampCapabilitiesMapper;
    private final IBootcampRequestDtoMapper bootcampRequestDtoMapper;

    @Bean
    public IBootcampServicePort bootcampServicePort(ICapabilityExternalPort capabilityExternalPort){
        return new BootcampUseCase(bootcampPersistencePort, capabilityExternalPort);
    }

    @Bean
    public ICapabilityExternalPort capabilityExternalPort(WebClient webClient) {
        return new CapabilityAdapter(webClient, bootcampCapabilitiesMapper,bootcampRequestDtoMapper);
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(capabilityProperties.getBaseUrl()).build();
    }
}
