package com.jaikeex.mywebpage.config.circuitbreaker;

import com.jaikeex.mywebpage.config.circuitbreaker.qualifier.CircuitBreakerName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfig {

    private static final String ISSUE_SERVICE_CIRCUIT_BREAKER_NAME = "ISSUE_SERVICE_CB";
    private static final String PROJECTS_SERVICE_CIRCUIT_BREAKER_NAME = "PROJECTS_SERVICE_CB";
    private static final String USER_SERVICE_CIRCUIT_BREAKER_NAME = "USER_SERVICE_CB";
    private static final String RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER = "RESET_PASSWORD_SERVICE_CB";

    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Autowired
    public CircuitBreakerConfig(CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Bean
    @CircuitBreakerName(ISSUE_SERVICE_CIRCUIT_BREAKER_NAME)
    public CircuitBreaker getIssueTrackerCircuitBreaker() {
        return circuitBreakerFactory.create(ISSUE_SERVICE_CIRCUIT_BREAKER_NAME);
    }

    @Bean
    @CircuitBreakerName(PROJECTS_SERVICE_CIRCUIT_BREAKER_NAME)
    public CircuitBreaker getProjectsServiceCircuitBreaker() {
        return circuitBreakerFactory.create(PROJECTS_SERVICE_CIRCUIT_BREAKER_NAME);
    }

    @Bean
    @CircuitBreakerName(USER_SERVICE_CIRCUIT_BREAKER_NAME)
    public CircuitBreaker getUserServiceCircuitBreaker() {
        return circuitBreakerFactory.create(USER_SERVICE_CIRCUIT_BREAKER_NAME);
    }

    @Bean
    @CircuitBreakerName(RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER)
    public CircuitBreaker getRegisterServiceCircuitBreaker() {
        return circuitBreakerFactory.create(RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER);
    }
}
