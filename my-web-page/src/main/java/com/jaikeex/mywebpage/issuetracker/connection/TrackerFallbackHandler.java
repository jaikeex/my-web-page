package com.jaikeex.mywebpage.issuetracker.connection;

import com.jaikeex.mywebpage.config.connection.FallbackHandler;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
@Slf4j
public class TrackerFallbackHandler implements FallbackHandler {

    @Override
    public <T> ResponseEntity<T> throwBackendServiceException(String fallbackMessage,
                                                              Throwable exception,
                                                              Class<? extends ServiceDownException> exceptionType) {
        checkForClientError(exception);
        throwMatchingException(fallbackMessage, exceptionType);
        return null;
    }

    private void throwMatchingException(String fallbackMessage, Class<? extends ServiceDownException> exceptionType) {
        try {
            throw exceptionType.getConstructor().newInstance();
        } catch (ReflectiveOperationException exception) {
            log.warn("Couldn't throw exception [exception={}]", exceptionType);
            log.warn("{}", (Object) exception.getStackTrace());
            throw new ServiceDownException(fallbackMessage);
        }
    }

    private void checkForClientError(Throwable throwable) {
        if (throwable instanceof HttpClientErrorException) {
            throw ((HttpClientErrorException) throwable);
        }
    }
}
