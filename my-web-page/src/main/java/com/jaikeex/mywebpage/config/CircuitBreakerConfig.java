package com.jaikeex.mywebpage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfig {

    private static final String CIRCUIT_BREAKER_NAME = "ISSUE_SERVICE_CB";

    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Autowired
    public CircuitBreakerConfig(CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Bean
    public CircuitBreaker getCircuitBreaker() {
        return circuitBreakerFactory.create(CIRCUIT_BREAKER_NAME);
    }
}
