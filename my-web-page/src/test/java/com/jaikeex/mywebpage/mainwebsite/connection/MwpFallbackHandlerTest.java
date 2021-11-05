package com.jaikeex.mywebpage.mainwebsite.connection;

import com.jaikeex.mywebpage.mainwebsite.service.UserService;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.UserServiceDownException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
                () -> fallbackHandler.throwFallbackException("fallback Message",
                        testException,
                        UserServiceDownException.class)
        );
    }

    @Test
    public void throwFallbackException_shouldCheckForClientError() {
        Exception testException = new HttpClientErrorException(HttpStatus.NOT_FOUND, "testException");
        assertThrows(
                HttpClientErrorException.class,
                () -> fallbackHandler.throwFallbackException("fallback Message",
                        testException,
                        UserServiceDownException.class)
        );
    }
}