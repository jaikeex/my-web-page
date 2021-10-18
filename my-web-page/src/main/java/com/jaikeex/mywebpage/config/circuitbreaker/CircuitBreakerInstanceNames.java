package com.jaikeex.mywebpage.config.circuitbreaker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

public class CircuitBreakerInstanceNames {
    public static final String ISSUE_SERVICE_CIRCUIT_BREAKER = "IssueService_CB";
    public static final String PROJECTS_SERVICE_CIRCUIT_BREAKER = "ProjectDetailsService_CB";
    public static final String USER_SERVICE_CIRCUIT_BREAKER = "UserService_CB";
    public static final String RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER = "ResetPasswordService_CB";
    public static final String CONTACT_SERVICE_CIRCUIT_BREAKER = "ContactService_CB";
}
