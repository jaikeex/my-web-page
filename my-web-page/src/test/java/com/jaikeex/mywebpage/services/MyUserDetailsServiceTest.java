package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.entity.User;
import com.jaikeex.mywebpage.jpa.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
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
    UserRepository repository;

    @InjectMocks
    MyUserDetailsService userDetailsService;

    @Test
    void loadUserByUsernameThrow() {
        String userName = "thisUserDoesNotExistInDatabase";
        when(repository.findByUsername(userName)).thenReturn(null);
        Exception exception = assertThrows(NullPointerException.class, () -> {
            userDetailsService.loadUserByUsername(userName);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(userName));
    }

    @Test
    void loadUserByUsername() {
        MyUserDetails testDetails1 = new MyUserDetails(testUser1);
        when(repository.findByUsername("testuserfordbaccess")).thenReturn(testUser1);
        UserDetails returnedDetails = userDetailsService.loadUserByUsername("testuserfordbaccess");
        assertEquals(testDetails1, returnedDetails);
    }
}