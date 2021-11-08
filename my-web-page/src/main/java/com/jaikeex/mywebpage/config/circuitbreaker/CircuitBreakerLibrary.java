package com.jaikeex.mywebpage.config.circuitbreaker;

import com.jaikeex.mywebpage.config.circuitbreaker.qualifier.CircuitBreakerName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.jaikeex.mywebpage.config.circuitbreaker.CircuitBreakerInstanceNames.*;

/**
 * Manages access to the circuit breaker beans.
 */
@Component
public class CircuitBreakerLibrary {

    private Map<String, CircuitBreaker> breakers;

    @Autowired
    public CircuitBreakerLibrary(@CircuitBreakerName(ISSUE_SERVICE_CIRCUIT_BREAKER)
                                             CircuitBreaker issueServiceCircuitBreaker,
                                 @CircuitBreakerName(PROJECTS_SERVICE_CIRCUIT_BREAKER)
                                             CircuitBreaker projectsServiceCircuitBreaker,
                                 @CircuitBreakerName(USER_SERVICE_CIRCUIT_BREAKER)
                                             CircuitBreaker userServiceCircuitBreaker,
                                 @CircuitBreakerName(USER_DETAILS_SERVICE_CIRCUIT_BREAKER)
                                             CircuitBreaker userDetailsServiceCircuitBreaker,
                                 @CircuitBreakerName(USER_DETAILS_SERVICE_CIRCUIT_BREAKER)
                                             CircuitBreaker resetPasswordServiceCircuitBreaker,
                                 @CircuitBreakerName(CONTACT_SERVICE_CIRCUIT_BREAKER)
                                             CircuitBreaker contactServiceCircuitBreaker) {
        breakers = initializeBreakersMap(
                issueServiceCircuitBreaker,
                projectsServiceCircuitBreaker,
                userServiceCircuitBreaker,
                userDetailsServiceCircuitBreaker,
                resetPasswordServiceCircuitBreaker,
                contactServiceCircuitBreaker);
    }

    public CircuitBreaker getCircuitBreaker(String name) {
        return breakers.get(name);
    }

    public Map<String, CircuitBreaker> getBreakers() {
        return breakers;
    }

    private Map<String, CircuitBreaker> initializeBreakersMap(
            CircuitBreaker issueServiceCircuitBreaker,
            CircuitBreaker projectsServiceCircuitBreaker,
            CircuitBreaker userServiceCircuitBreaker,
            CircuitBreaker userDetailsServiceCirtcuitBreaker,
            CircuitBreaker resetPasswordServiceCircuitBreaker,
            CircuitBreaker contactServiceCircuitBreaker) {
        this.breakers = new HashMap<>();
        breakers.put(ISSUE_SERVICE_CIRCUIT_BREAKER, issueServiceCircuitBreaker);
        breakers.put(PROJECTS_SERVICE_CIRCUIT_BREAKER, projectsServiceCircuitBreaker);
        breakers.put(USER_SERVICE_CIRCUIT_BREAKER, userServiceCircuitBreaker);
        breakers.put(USER_DETAILS_SERVICE_CIRCUIT_BREAKER, userDetailsServiceCirtcuitBreaker);
        breakers.put(RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER, resetPasswordServiceCircuitBreaker);
        breakers.put(CONTACT_SERVICE_CIRCUIT_BREAKER, contactServiceCircuitBreaker);
        return breakers;
    }
}
