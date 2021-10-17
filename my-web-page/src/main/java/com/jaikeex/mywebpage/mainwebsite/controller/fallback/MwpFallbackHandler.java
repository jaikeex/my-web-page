package com.jaikeex.mywebpage.mainwebsite.controller.fallback;

import com.jaikeex.mywebpage.config.circuitbreaker.FallbackHandler;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.client.HttpClientErrorException;

@Component
@Slf4j
public class MwpFallbackHandler implements FallbackHandler {

    @Override
    public <T> ResponseEntity<T> throwFallbackException(String fallbackMessage,
                                                        Throwable exception,
                                                        Class<? extends ServiceDownException> exceptionType) {
        log.warn("{}", ExceptionUtils.getStackTrace(exception));
        checkForClientError(exception);
        throwMatchingException(fallbackMessage, exceptionType);
        return null;
    }

    private void throwMatchingException(String fallbackMessage, Class<? extends ServiceDownException> exceptionType) {
        try {
            throw exceptionType.getConstructor(String.class).newInstance(fallbackMessage);
        } catch (ReflectiveOperationException exception) {
            log.warn("Couldn't throw exception [exception={}]", exceptionType);
            log.warn("{}", (Object) exception.getStackTrace());
            throw new ServiceDownException();
        }
    }

    private void checkForClientError(Throwable throwable) {
        if (throwable instanceof HttpClientErrorException) {
            throw ((HttpClientErrorException) throwable);
        }
    }
}
