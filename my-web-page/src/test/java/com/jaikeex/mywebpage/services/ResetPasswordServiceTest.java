package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.model.User;
import com.jaikeex.mywebpage.restemplate.RestTemplateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@TestComponent
@ExtendWith(SpringExtension.class)
class ResetPasswordServiceTest {
    private final User testUser1 = new User();


    private final ResetPasswordDto resetPasswordDto = new ResetPasswordDto();


    @Mock
    RestTemplateFactory restTemplateFactory;
    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    ResetPasswordService service;

    @BeforeEach
    public void beforeEach() {
        when(restTemplateFactory.getRestTemplate()).thenReturn(restTemplate);
        resetPasswordDto.setPassword("testPassword");
        resetPasswordDto.setPasswordForValidation("testPassword");
        resetPasswordDto.setEmail("testEmail");
        resetPasswordDto.setToken("testToken");
        resetPasswordDto.setResetLink("testResetLink");
    }

    @Test
    public void resetPassword_givenValidData_shouldCallUserService() {
        service.resetPassword(resetPasswordDto);
        verify(restTemplate, times(1))
                .patchForObject(anyString(), anyString(), any());
    }

    @Test
    public void resetPassword_shouldEncodePassword() {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        service.resetPassword(resetPasswordDto);
        verify(restTemplate)
                .patchForObject(anyString(), argument.capture(), any());
        assertTrue(argument.getValue().contains("argon"));
    }

    @Test
    public void sendConfirmationEmail_givenValidData_shouldCallUserService() {
        service.sendConfirmationEmail("testEmail");
        verify(restTemplate, times(1))
                .getForEntity(anyString(), any());
    }

    @Test
    public void sendConfirmationEmail_shouldPassCorrectArgument() {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        service.sendConfirmationEmail("testEmail");
        verify(restTemplate)
                .getForEntity(argument.capture(), any());
        assertTrue(argument.getValue().contains("testEmail"));
    }
}