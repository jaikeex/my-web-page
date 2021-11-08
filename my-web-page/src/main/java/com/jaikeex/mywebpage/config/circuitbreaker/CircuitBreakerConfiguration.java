package com.jaikeex.mywebpage.config.circuitbreaker;

import com.jaikeex.mywebpage.config.circuitbreaker.qualifier.CircuitBreakerName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.jaikeex.mywebpage.config.circuitbreaker.CircuitBreakerInstanceNames.*;

/**
 * Declares Beans of circuit breakers for each Service.
 */
@Configuration
public class CircuitBreakerConfiguration {

    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Autowired
    public CircuitBreakerConfiguration(CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
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
    @CircuitBreakerName(USER_DETAILS_SERVICE_CIRCUIT_BREAKER)
    public CircuitBreaker getUserDetailsServiceCircuitBreaker() {
        return circuitBreakerFactory.create(USER_DETAILS_SERVICE_CIRCUIT_BREAKER);
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
