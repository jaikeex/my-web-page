package com.jaikeex.mywebpage.config.connection;

import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import org.springframework.http.ResponseEntity;


/**
 * Processes any exceptions thrown by the microservices.
 */
public interface FallbackHandler {

    <T> ResponseEntity<T> throwBackendServiceException(String fallbackMessage,
                                                       Throwable throwable,
                                                       Class<? extends ServiceDownException> exceptionType);
}
