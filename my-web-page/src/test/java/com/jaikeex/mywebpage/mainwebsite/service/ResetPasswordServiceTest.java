package com.jaikeex.mywebpage.mainwebsite.service;

import com.jaikeex.mywebpage.mainwebsite.connection.MwpServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.dto.ResetPasswordDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@TestComponent
@ExtendWith(SpringExtension.class)
class ResetPasswordServiceTest {
    private final ResetPasswordDto resetPasswordDto = new ResetPasswordDto();

    @Mock
    MwpServiceRequest serviceRequest;
    
    @InjectMocks
    ResetPasswordService service;

    @BeforeEach
    public void beforeEach() {
        resetPasswordDto.setPassword("testPassword");
        resetPasswordDto.setPasswordForValidation("testPassword");
        resetPasswordDto.setEmail("testEmail");
        resetPasswordDto.setToken("testToken");
        resetPasswordDto.setResetLink("testResetLink");
    }

    @Test
    public void resetPassword_givenValidData_shouldCallUserService() {
        service.resetPassword(resetPasswordDto);
        verify(serviceRequest, times(1))
                .sendPatchRequest(anyString(), anyString(), any());
    }

    @Test
    public void resetPassword_shouldEncodePassword() {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        service.resetPassword(resetPasswordDto);
        verify(serviceRequest)
                .sendPatchRequest(anyString(), argument.capture(), any());
        assertTrue(argument.getValue().contains("argon"));
    }

    @Test
    public void sendConfirmationEmail_givenValidData_shouldCallUserService() {
        service.sendConfirmationEmail("testEmail");
        verify(serviceRequest, times(1))
                .sendGetRequest(anyString(), any(), any());
    }

    @Test
    public void sendConfirmationEmail_shouldPassCorrectArgument() {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        service.sendConfirmationEmail("testEmail");
        verify(serviceRequest)
                .sendGetRequest(argument.capture(), any(), any());
        assertTrue(argument.getValue().contains("testEmail"));
    }
}