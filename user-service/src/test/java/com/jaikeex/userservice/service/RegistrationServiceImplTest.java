package com.jaikeex.userservice.service;

import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.repository.UserRepository;
import com.jaikeex.userservice.service.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class RegistrationServiceImplTest {

    User testUser = new User();

    @Mock
    UserRepository repository;

    @InjectMocks
    RegistrationServiceImpl service;

    @BeforeEach
    public void beforeEach() {
        when(repository.save(any(User.class))).thenReturn(new User());
        testUser.setEmail("");
        testUser.setUsername("");
    }

    @Test
    public void registerUser_givenOriginalUserData_shouldReturnUser()
            throws UserAlreadyExistsException {
        when(repository.findUserByUsername(anyString())).thenReturn(null);
        when(repository.findUserByEmail(anyString())).thenReturn(null);
        assertNotNull(service.registerUser(testUser));
    }

    @Test
    public void registerUser_givenUsedUsername_shouldReturnNull() {
        when(repository.findUserByUsername(anyString())).thenReturn(new User());
        when(repository.findUserByEmail(anyString())).thenReturn(null);
        assertThrows(Exception.class, () -> service.registerUser(testUser));
    }

    @Test
    public void registerUser_givenUsedEmail_shouldReturnNull() {
        when(repository.findUserByUsername(anyString())).thenReturn(null);
        when(repository.findUserByEmail(anyString())).thenReturn(new User());
        assertThrows(Exception.class, () -> service.registerUser(testUser));
    }

    @Test
    public void registerUser_givenOriginalUserData_shouldSaveUserToDatabase()
            throws UserAlreadyExistsException {
        when(repository.findUserByUsername(anyString())).thenReturn(null);
        when(repository.findUserByEmail(anyString())).thenReturn(null);
        service.registerUser(testUser);
        verify(repository, times(1)).save(testUser);
    }
}