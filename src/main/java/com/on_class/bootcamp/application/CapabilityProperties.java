package com.on_class.bootcamp.application;

import com.on_class.bootcamp.infrastructure.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = Constants.PROPERTIES_PREFIX_TECHNOLOGY)
@Getter
@Setter
public class CapabilityProperties {

    private String baseUrl;
}
