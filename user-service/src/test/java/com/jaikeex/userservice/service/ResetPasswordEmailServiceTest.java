package com.jaikeex.userservice.service;

import static org.junit.jupiter.api.Assertions.*;

import com.jaikeex.userservice.dto.Email;
import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.repository.UserRepository;
import com.jaikeex.userservice.restemplate.RestTemplateFactory;
import com.jaikeex.userservice.service.exception.EmailServiceDownException;
import com.jaikeex.userservice.service.exception.NoSuchUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class ResetPasswordEmailServiceTest {

    @Mock
    UserService userService;
    @Mock
    RestTemplate restTemplate;
    @Mock
    RestTemplateFactory restTemplateFactory;
    @Mock
    HttpServerErrorException httpServerErrorException;

    @InjectMocks
    ResetPasswordEmailService resetPasswordEmailService;

    User testUser = new User();

    @BeforeEach
    public void beforeEach() {
        testUser.setEmail("email");
        testUser.setUsername("username");
        testUser.setResetPasswordToken("token");
        httpServerErrorException = new HttpServerErrorException(
                HttpStatus.INTERNAL_SERVER_ERROR, "");
        when(restTemplateFactory.getRestTemplate()).thenReturn(restTemplate);

    }

    @Test
    public void sendResetPasswordConfirmationEmail_givenInvalidEmail_shouldThrowException() throws NoSuchUserException {
        doThrow(NoSuchUserException.class).when(userService)
                .saveUserWithEncodedResetTokenToDatabase(anyString(), anyString());
        assertThrows(NoSuchUserException.class,
                () -> resetPasswordEmailService.sendResetPasswordConfirmationEmail(""));
    }

    @Test
    public void sendResetPasswordConfirmationEmail_givenValidEmail_shouldMakeRestCall()
            throws Exception {
        resetPasswordEmailService.sendResetPasswordConfirmationEmail("email");
        verify(restTemplate, times(1))
                .postForEntity(anyString(), any(Email.class), any());
    }

    @Test
    public void sendResetPasswordConfirmationEmail_givenEmailServiceFails_shouldThrowException() {
        when(restTemplate.postForEntity(anyString(), any(Email.class), any()))
                .thenThrow(httpServerErrorException);
        assertThrows(EmailServiceDownException.class,
                () -> resetPasswordEmailService.sendResetPasswordConfirmationEmail("email"));
    }


    @Test
    public void sendResetPasswordConfirmationEmail_emailShouldIncludeToken()
            throws Exception {
        ArgumentCaptor<Email> argument = ArgumentCaptor.forClass(Email.class);
        resetPasswordEmailService.sendResetPasswordConfirmationEmail("email");
        verify(restTemplate).postForEntity(anyString(), argument.capture(), any());
        assertTrue(argument.getValue().getMessage().contains("token"));
    }

    @Test
    public void sendResetPasswordConfirmationEmail_emailShouldContainLink()
            throws Exception {
        ArgumentCaptor<Email> argument = ArgumentCaptor.forClass(Email.class);
        resetPasswordEmailService.sendResetPasswordConfirmationEmail("email");
        verify(restTemplate).postForEntity(anyString(), argument.capture(), any());
        assertTrue(argument.getValue().getMessage().contains("https://www.kubahruby.com"));
    }

    @Test
    public void sendResetPasswordConfirmationEmail_emailShouldHaveCorrectRecipient()
            throws Exception {
        ArgumentCaptor<Email> argument = ArgumentCaptor.forClass(Email.class);
        resetPasswordEmailService.sendResetPasswordConfirmationEmail("email");
        verify(restTemplate).postForEntity(anyString(), argument.capture(), any());
        assertTrue(argument.getValue().getRecipient().contains("email"));
    }

    @Test
    public void sendResetPasswordConfirmationEmail_emailShouldIncludeSubject()
            throws Exception {
        ArgumentCaptor<Email> argument = ArgumentCaptor.forClass(Email.class);
        resetPasswordEmailService.sendResetPasswordConfirmationEmail("email");
        verify(restTemplate).postForEntity(anyString(), argument.capture(), any());
        assertTrue(argument.getValue().getSubject().toLowerCase().contains("password"));
    }

    @Test
    public void sendResetPasswordConfirmationEmail_givenAllOk_shouldSaveUserToDatabase()
            throws Exception {
        resetPasswordEmailService.sendResetPasswordConfirmationEmail("email");
        verify(userService, times(1))
                .saveUserWithEncodedResetTokenToDatabase(anyString(), anyString());
    }
/*
    @Test
    public void sendResetPasswordConfirmationEmail_givenNullArgument_shouldThrowException() throws NoSuchUserException {
        doThrow(NoSuchUserException.class).when(userService)
                .saveUserWithEncodedResetTokenToDatabase(any(null), anyString());
        assertThrows(NoSuchUserException.class,
                () -> resetPasswordEmailService.sendResetPasswordConfirmationEmail(null));
    }*/
}

















