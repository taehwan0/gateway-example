package com.example.gateway;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Pre Filter
        OrderedGatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (config.isPreLogger()) {
                log.info("LoggingFilter PreLog RequestId: {}, BaseMessage: {}",
                        request.getId(),
                        config.getBaseMessage());
            }

            return chain
                    .filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                                if (config.isPostLogger()) {
                                    log.info("LoggingFilter PostLog ResponseStatusCode: {}", response.getStatusCode());
                                }
                            }
                    ));
        }, Ordered.LOWEST_PRECEDENCE);

        return filter;
    }

    @Data
    public static class Config {
        // Configuration Properties
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
