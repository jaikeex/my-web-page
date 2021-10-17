package com.jaikeex.mywebpage.config.circuitbreaker;

import com.jaikeex.mywebpage.config.circuitbreaker.qualifier.CircuitBreakerName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CircuitBreakerLibrary {

    private static final String ISSUE_SERVICE_CIRCUIT_BREAKER = "IssueService_CB";
    private static final String PROJECTS_SERVICE_CIRCUIT_BREAKER = "ProjectDetailsService_CB";
    private static final String USER_SERVICE_CIRCUIT_BREAKER = "UserService_CB";
    private static final String RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER = "ResetPasswordService_CB";
    private static final String CONTACT_SERVICE_CIRCUIT_BREAKER = "ContactService_CB";

    private Map<String, CircuitBreaker> breakers;

    @Autowired
    public CircuitBreakerLibrary(@CircuitBreakerName(ISSUE_SERVICE_CIRCUIT_BREAKER) CircuitBreaker issueServiceCircuitBreaker,
                                 @CircuitBreakerName(PROJECTS_SERVICE_CIRCUIT_BREAKER) CircuitBreaker projectsServiceCircuitBreaker,
                                 @CircuitBreakerName(USER_SERVICE_CIRCUIT_BREAKER) CircuitBreaker userServiceCircuitBreaker,
                                 @CircuitBreakerName(RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER) CircuitBreaker resetPasswordServiceCircuitBreaker,
                                 @CircuitBreakerName(CONTACT_SERVICE_CIRCUIT_BREAKER) CircuitBreaker contactServiceCircuitBreaker) {
        breakers = initializeBreakersMap(
                issueServiceCircuitBreaker,
                projectsServiceCircuitBreaker,
                userServiceCircuitBreaker,
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
            CircuitBreaker resetPasswordServiceCircuitBreaker,
            CircuitBreaker contactServiceCircuitBreaker) {
        this.breakers = new HashMap<>();
        breakers.put(ISSUE_SERVICE_CIRCUIT_BREAKER, issueServiceCircuitBreaker);
        breakers.put(PROJECTS_SERVICE_CIRCUIT_BREAKER, projectsServiceCircuitBreaker);
        breakers.put(USER_SERVICE_CIRCUIT_BREAKER, userServiceCircuitBreaker);
        breakers.put(RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER, resetPasswordServiceCircuitBreaker);
        breakers.put(CONTACT_SERVICE_CIRCUIT_BREAKER, contactServiceCircuitBreaker);
        return breakers;
    }
}
