package com.jaikeex.mywebpage.config.circuitbreaker;

import com.jaikeex.mywebpage.config.circuitbreaker.qualifier.CircuitBreakerName;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static com.jaikeex.mywebpage.config.circuitbreaker.CircuitBreakerInstanceNames.*;

@Configuration
public class CircuitBreakerConfiguration {



    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;
    private final CircuitBreakerConfig config;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    @Autowired
    public CircuitBreakerConfiguration(CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.circuitBreakerRegistry = CircuitBreakerRegistry.of(getConfig());
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.config = getConfig();
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

    private CircuitBreakerConfig getConfig() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slowCallRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .permittedNumberOfCallsInHalfOpenState(3)
                .minimumNumberOfCalls(10)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                .slidingWindowSize(5)
                .build();
    }

}
