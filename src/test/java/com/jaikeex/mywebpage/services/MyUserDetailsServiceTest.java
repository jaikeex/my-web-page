package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.entity.User;
import com.jaikeex.mywebpage.jpa.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class MyUserDetailsServiceTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    MyUserDetailsService userDetailsService;

    private final User testUser1 = new User(1, "kuba", "$argon2id$v=19$m=65536,t=3,p=1$ZzXUj9kdYQm/s7lFErt4wQ$XPemiyn0Pb2Vl9QlD37hWhxn9t1H8vadVUpuE8FGmzM",
            null, null, null, null, null, true, "ADMIN,USER");

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
        when(repository.findByUsername("kuba")).thenReturn(testUser1);
        UserDetails returnedDetails = userDetailsService.loadUserByUsername("kuba");
        assertEquals(testDetails1, returnedDetails);
    }
}