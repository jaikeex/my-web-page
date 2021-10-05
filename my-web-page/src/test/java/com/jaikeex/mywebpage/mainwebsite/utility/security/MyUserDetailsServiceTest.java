package com.jaikeex.mywebpage.mainwebsite.utility.security;

import com.jaikeex.mywebpage.mainwebsite.model.User;
import com.jaikeex.mywebpage.resttemplate.RestTemplateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestComponent
@ExtendWith(SpringExtension.class)
class MyUserDetailsServiceTest {

    private final User testUser1 = new User(
            1,
            "testuserfordbaccess",
            "$argon2id$v=19$m=65536,t=3,p=1$peMkKGWTfioAQols1mso3A$dG2V75p0v6onSrFT9kOtMqhwmqOCsySt6la1QYtH2Jc",
            "testuserfordbaccess@testuserfordbaccess.com",
            "$argon2id$v=19$m=65536,t=3,p=1$ZRzpyukFgnnO1m6bkadOGA$2RxS99w4CBZVGQ/vXy8TMbr7VvXcifba2FCJePEyA/4",
            null,
            null,
            null,
            true,
            "USER");

    @Mock
    RestTemplateFactory restTemplateFactory;
    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    MyUserDetailsService service;

    @BeforeEach
    public void beforeEach() {
        when(restTemplateFactory.getRestTemplate()).thenReturn(restTemplate);
    }

    @Test
    void loadUserByUsername_shouldHandleHttpClientErrorException() {
        when(restTemplate.getForObject(anyString(), any())).thenThrow(HttpClientErrorException.class);
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("testUsername"));
    }

    @Test
    void loadUserByUsername() {
        MyUserDetails testDetails1 = new MyUserDetails(testUser1);
        when(restTemplate.getForObject(anyString(), any())).thenReturn(testUser1);
        UserDetails returnedDetails = service.loadUserByUsername("testuserfordbaccess");
        assertEquals(testDetails1, returnedDetails);
    }
}