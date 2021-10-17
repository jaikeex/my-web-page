package com.jaikeex.mywebpage.config.circuitbreaker;

import com.jaikeex.mywebpage.config.circuitbreaker.qualifier.CircuitBreakerName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfig {

    private static final String ISSUE_SERVICE_CIRCUIT_BREAKER = "IssueService_CB";
    private static final String PROJECTS_SERVICE_CIRCUIT_BREAKER = "ProjectDetailsService_CB";
    private static final String USER_SERVICE_CIRCUIT_BREAKER = "UserService_CB";
    private static final String RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER = "ResetPasswordService_CB";
    private static final String CONTACT_SERVICE_CIRCUIT_BREAKER = "ContactService_CB";

    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Autowired
    public CircuitBreakerConfig(CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Bean
    @CircuitBreakerName(ISSUE_SERVICE_CIRCUIT_BREAKER)
    public CircuitBreaker getIssueTrackerCircuitBreaker() {
        return circuitBreakerFactory.create(ISSUE_SERVICE_CIRCUIT_BREAKER);
    }

    @Bean
    @CircuitBreakerName(PROJECTS_SERVICE_CIRCUIT_BREAKER)
    public CircuitBreaker getProjectsServiceCircuitBreaker() {
        return circuitBreakerFactory.create(PROJECTS_SERVICE_CIRCUIT_BREAKER);
    }

    @Bean
    @CircuitBreakerName(USER_SERVICE_CIRCUIT_BREAKER)
    public CircuitBreaker getUserServiceCircuitBreaker() {
        return circuitBreakerFactory.create(USER_SERVICE_CIRCUIT_BREAKER);
    }

    @Bean
    @CircuitBreakerName(RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER)
    public CircuitBreaker getResetPasswordServiceCircuitBreaker() {
        return circuitBreakerFactory.create(RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER);
    }

    @Bean
    @CircuitBreakerName(CONTACT_SERVICE_CIRCUIT_BREAKER)
    public CircuitBreaker getContactServiceCircuitBreaker() {
        return circuitBreakerFactory.create(CONTACT_SERVICE_CIRCUIT_BREAKER);
    }
}
