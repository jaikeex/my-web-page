package com.jaikeex.mywebpage.config.connection;

import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import org.springframework.http.ResponseEntity;


public interface FallbackHandler {

    <T> ResponseEntity<T> throwFallbackException(String fallbackMessage,
                                                Throwable throwable,
                                                Class<? extends ServiceDownException> exceptionType);
}
