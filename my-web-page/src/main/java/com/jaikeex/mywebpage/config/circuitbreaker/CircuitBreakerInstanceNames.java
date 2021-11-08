package com.jaikeex.mywebpage.config.circuitbreaker;

/**
 * Holds Names of all the circuit breakers. This apparently can't be stored
 * in the application.yml file due to the fact that annotation parameters
 * must be known to the compiler.
 */
public class CircuitBreakerInstanceNames {
    public static final String ISSUE_SERVICE_CIRCUIT_BREAKER = "IssueServiceImpl_CB";
    public static final String PROJECTS_SERVICE_CIRCUIT_BREAKER = "ProjectDetailsServiceImpl_CB";
    public static final String USER_SERVICE_CIRCUIT_BREAKER = "UserServiceImpl_CB";
    public static final String USER_DETAILS_SERVICE_CIRCUIT_BREAKER = "MyUserDetailsService_CB";
    public static final String RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER = "ResetPasswordServiceImpl_CB";
    public static final String CONTACT_SERVICE_CIRCUIT_BREAKER = "ContactServiceImpl_CB";
}
