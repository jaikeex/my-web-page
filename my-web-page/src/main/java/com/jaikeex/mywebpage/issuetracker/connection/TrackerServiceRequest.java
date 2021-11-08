package com.jaikeex.mywebpage.issuetracker.connection;

import com.jaikeex.mywebpage.config.circuitbreaker.CircuitBreakerLibrary;
import com.jaikeex.mywebpage.config.connection.ServiceRequest;
import com.jaikeex.mywebpage.config.connection.resttemplate.RestTemplateFactory;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class TrackerServiceRequest implements ServiceRequest {

    private static final String FALLBACK_MESSAGE = "Requested service is unavailable.";
    private static final String CIRCUIT_BREAKER_NAME_SUFFIX = "_CB";

    private final RestTemplate restTemplate;
    private final TrackerFallbackHandler fallbackHandler;
    private final Map<String, CircuitBreaker> circuitBreakers;

    @Autowired
    public TrackerServiceRequest(CircuitBreakerLibrary circuitBreakerLibrary,
                                 RestTemplateFactory restTemplateFactory,
                                 TrackerFallbackHandler fallbackHandler) {
        this.restTemplate = restTemplateFactory.getRestTemplate();
        this.fallbackHandler = fallbackHandler;
        this.circuitBreakers = circuitBreakerLibrary.getBreakers();
    }

    @Override
    public <T> ResponseEntity<T> sendGetRequest(String url, Class<T> responseType, Class<? extends ServiceDownException> exceptionType) {
        CircuitBreaker circuitBreaker = getCircuitBreaker();
        return circuitBreaker.run(
                () -> restTemplate.getForEntity(url, responseType),
                throwable -> fallbackHandler.throwBackendServiceException(FALLBACK_MESSAGE, throwable, exceptionType));
    }

    @Override
    public void sendPostRequest(String url, Object body, Class<? extends ServiceDownException> exceptionType) {
        CircuitBreaker circuitBreaker = getCircuitBreaker();
        circuitBreaker.run(
                () -> restTemplate.postForEntity(url, body, Object.class),
                throwable -> fallbackHandler.throwBackendServiceException(FALLBACK_MESSAGE, throwable, exceptionType));
    }

    @Override
    public void sendPatchRequest(String url, Object body, Class<? extends ServiceDownException> exceptionType) {
        CircuitBreaker circuitBreaker = getCircuitBreaker();
        circuitBreaker.run(
                () -> restTemplate.patchForObject(url, body, Object.class),
                throwable -> fallbackHandler.throwBackendServiceException(FALLBACK_MESSAGE, throwable, exceptionType));
    }

    private CircuitBreaker getCircuitBreaker() {
        String circuitBreakerName = getCircuitBreakerName();
        return circuitBreakers.get(circuitBreakerName);
    }

    private String getCircuitBreakerName() {
        String fullClassName = Thread.currentThread().getStackTrace()[4].getClassName();
        String[] classNameAsArray = fullClassName.split("\\.");
        String className = classNameAsArray[classNameAsArray.length - 1];
        return className + CIRCUIT_BREAKER_NAME_SUFFIX;
    }
}
