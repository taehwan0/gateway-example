package com.example.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {

//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(r -> r
                        .path("/first-service/**")
                        .filters(f -> f
                                .addRequestHeader("first-request", "first-request-value")
                                .addResponseHeader("first-response", "first-response-value"))
                        .uri("http://localhost:8001"))
                .route(r -> r
                        .path("/second-service/**")
                        .filters(f -> f
                                .addRequestHeader("second-request", "second-request-value")
                                .addResponseHeader("second-response", "second-response-value"))
                        .uri("http://localhost:8002"))
                .build();
    }
}
