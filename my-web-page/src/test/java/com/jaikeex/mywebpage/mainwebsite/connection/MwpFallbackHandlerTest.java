package com.jaikeex.mywebpage.mainwebsite.connection;

import com.jaikeex.mywebpage.mainwebsite.utility.exception.UserServiceDownException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MwpFallbackHandlerTest {

    @Autowired
    MwpFallbackHandler fallbackHandler;

    @Test
    public void throwFallbackException_shouldThrowMatchingException() {
        Exception testException = new Exception("testException");
        assertThrows(
                UserServiceDownException.class,
                () -> fallbackHandler.throwBackendServiceException("fallback Message",
                        testException,
                        UserServiceDownException.class)
        );
    }

    @Test
    public void throwFallbackException_shouldCheckForClientError() {
        Exception testException = new HttpClientErrorException(HttpStatus.NOT_FOUND, "testException");
        assertThrows(
                HttpClientErrorException.class,
                () -> fallbackHandler.throwBackendServiceException("fallback Message",
                        testException,
                        UserServiceDownException.class)
        );
    }
}