package com.on_class.bootcamp.infrastructure.entrypoints;

import com.on_class.bootcamp.infrastructure.entrypoints.documentation.BootcampApiInfo;
import com.on_class.bootcamp.infrastructure.entrypoints.handler.BootcampHandler;
import com.on_class.bootcamp.infrastructure.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BootcampRouter {
    @Bean
    @BootcampApiInfo
    public RouterFunction<ServerResponse> bootcampRoutes(BootcampHandler bootcampHandler) {
        return route(POST(Constants.ROUTE_BOOTCAMP),
                     bootcampHandler::createBootcamp);
    }
}