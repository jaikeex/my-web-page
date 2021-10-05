package com.jaikeex.userservice.service;

import static org.junit.jupiter.api.Assertions.*;

import com.jaikeex.userservice.dto.ResetPasswordDto;
import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.repository.UserRepository;
import com.jaikeex.userservice.service.exception.InvalidResetTokenException;
import com.jaikeex.userservice.service.exception.NoSuchUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Mock
    UserRepository repository;
    @Mock
    RegistrationService registrationService;
    @Mock
    MyPasswordEncoder passwordEncoder;

    @InjectMocks
    UserService service;

    private User testUser;
    private ResetPasswordDto resetPasswordDto;

    @BeforeEach
    public void beforeEach() {
        testUser = new User();
        testUser.setResetPasswordToken("resetToken");

        resetPasswordDto = new ResetPasswordDto();
        resetPasswordDto.setPassword("password");
        resetPasswordDto.setResetToken("resetToken");

    }

    @Test
    public void findUserByEmail_givenValidEmail_shouldReturnUser() {
        when(repository.findUserByEmail(anyString())).thenReturn(testUser);
        assertEquals(testUser, service.findUserByEmail("email"));
    }

    @Test
    public void findUserByEmail_givenInvalidEmail_shouldReturnNull() {
        when(repository.findUserByEmail(anyString())).thenReturn(null);
        assertNull(service.findUserByEmail("email"));
    }

    @Test
    public void findUserById_givenValidId_shouldReturnUser() {
        when(repository.findUserById(anyInt())).thenReturn(testUser);
        assertEquals(testUser, service.findUserById(1));
    }

    @Test
    public void findUserById_givenInvalidId_shouldReturnNull() {
        when(repository.findUserById(anyInt())).thenReturn(null);
        assertNull(service.findUserById(1));
    }

    @Test
    public void findUserByUsername_givenValidUsername_shouldReturnUser() {
        when(repository.findUserByUsername(anyString())).thenReturn(testUser);
        assertEquals(testUser, service.findUserByUsername("username"));
    }

    @Test
    public void findUserByUsername_givenInvalidUsername_shouldReturnNull() {
        when(repository.findUserByUsername(anyString())).thenReturn(null);
        assertNull(service.findUserByUsername("username"));
    }

    @Test
    public void updatePasswordOfUser_givenValidData_shouldReturnUser() throws InvalidResetTokenException, NoSuchUserException {
        when(repository.findUserByEmail(anyString())).thenReturn(testUser);
        when(repository.findUserById(anyInt())).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        assertEquals(
                testUser,
                service.updatePasswordOfUser("email", resetPasswordDto));
        assertEquals(
                testUser,
                service.updatePasswordOfUser(1, resetPasswordDto));

    }

    @Test
    public void updatePasswordOfUser_givenInvalidIdentifier_shouldThrowException() throws InvalidResetTokenException {
        when(repository.findUserByEmail(anyString())).thenReturn(null);
        when(repository.findUserById(anyInt())).thenReturn(null);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        assertThrows(NoSuchUserException.class,
                () -> service.updatePasswordOfUser("email", resetPasswordDto));
        assertThrows(NoSuchUserException.class,
                () -> service.updatePasswordOfUser(1, resetPasswordDto));
    }

    @Test
    public void updatePasswordOfUser_givenInvalidToken_shouldThrowException() throws InvalidResetTokenException {
        when(repository.findUserByEmail(anyString())).thenReturn(testUser);
        when(repository.findUserById(anyInt())).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        assertThrows(InvalidResetTokenException.class,
                () -> service.updatePasswordOfUser("email", resetPasswordDto));
        assertThrows(InvalidResetTokenException.class,
                () -> service.updatePasswordOfUser(1, resetPasswordDto));
    }

    @Test
    public void updatePasswordOfUserByEmail_givenValidData_shouldSaveUserToDatabase() throws InvalidResetTokenException, NoSuchUserException {
        when(repository.findUserByEmail(anyString())).thenReturn(testUser);
        when(repository.findUserById(anyInt())).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        service.updatePasswordOfUser("email", resetPasswordDto);
        verify(repository, times(1)).updatePasswordById(anyInt(), anyString());
    }

    @Test
    public void updatePasswordOfUserById_givenValidData_shouldSaveUserToDatabase() throws InvalidResetTokenException, NoSuchUserException {
        when(repository.findUserByEmail(anyString())).thenReturn(testUser);
        when(repository.findUserById(anyInt())).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        service.updatePasswordOfUser(1, resetPasswordDto);
        verify(repository, times(1)).updatePasswordById(anyInt(), anyString());
    }

    @Test
    public void updateLastAccessDateOfUserByUSername_shouldUpdateLastAccessDateOfUser() throws InvalidResetTokenException, NoSuchUserException {
        service.updateLastAccessDateOfUser("username", new Timestamp(0));
        verify(repository, times(1)).updateLastAccessDateByUsername(any(Timestamp.class), anyString());
    }

    @Test
    public void updateLastAccessDateOfUserById_shouldUpdateLastAccessDateOfUser() throws InvalidResetTokenException, NoSuchUserException {
        service.updateLastAccessDateOfUser(1, new Timestamp(0));
        verify(repository, times(1)).updateLastAccessDateById(any(Timestamp.class), anyInt());
    }
/*
    @Test
    public void sendResetPasswordConfirmationEmail_shouldSaveUserWithEncodedToken()
            throws Exception {
        String encodedToken = "encodedToken";
        //when(userService.saveUserWithEncodedResetTokenToDatabase(anyString(), anyString()));
        resetPasswordEmailService.sendResetPasswordConfirmationEmail("email");
        assertEquals(encodedToken, testUser.getResetPasswordToken());
    }
*/
}