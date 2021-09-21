package com.jaikeex.mywebpage.controllers;

import com.jaikeex.mywebpage.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.dto.ResetPasswordEmailDto;
import com.jaikeex.mywebpage.services.ResetPasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ResetPasswordControllerTest {
    @MockBean
    ResetPasswordService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    private final ResetPasswordDto resetPasswordDto = new ResetPasswordDto(
            "testuserfordbaccess@testuserfordbaccess.com",
            "rCkE4HajtcoG2PIRpZk2qsYt5ZJwl1gU",
            "testuserfordbaccess",
            "testuserfordbaccess");

    private final ResetPasswordEmailDto resetPasswordEmailDto =
            new ResetPasswordEmailDto("email@email.com");

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void getResetPasswordPage_shouldAddDtosToModel() throws Exception {
        mockMvc.perform(get("/user/reset-password"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("resetPasswordDto"))
                .andExpect(model().attributeExists("resetPasswordEmailDto"));
    }

    @Test
    public void postResetPasswordForm_givenAllOk_shouldSendConfirmationEmail() throws Exception {
        mockMvc.perform(post("/user/reset-password")
                .with(csrf())
                .flashAttr("resetPasswordEmailDto", resetPasswordEmailDto))
                .andExpect(status().isOk());
                verify(service).sendConfirmationEmail(anyString());
    }

    @Test
    public void postResetPasswordForm_givenAllOk_shouldAddSuccessAttributeToModel() throws Exception {
        mockMvc.perform(post("/user/reset-password")
                .with(csrf())
                .flashAttr("resetPasswordEmailDto", resetPasswordEmailDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute("success", true));
    }

    @Test
    public void postResetPasswordForm_givenSendingError_shouldCatchHttpServerErrorException() throws Exception {
        HttpServerErrorException exception =
                new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "testException");
        doThrow(exception)
                .when(service).sendConfirmationEmail(anyString());
        mockMvc.perform(post("/user/reset-password")
                .with(csrf())
                .flashAttr("resetPasswordEmailDto", resetPasswordEmailDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute("success", false))
                .andExpect(model().attributeExists("message"));
    }

    @Test
    public void postResetPasswordForm_givenSendingError_shouldCatchHttpClientErrorException() throws Exception {
        HttpClientErrorException exception =
                new HttpClientErrorException(HttpStatus.BAD_REQUEST, "testException");
        doThrow(exception)
                .when(service).sendConfirmationEmail(anyString());
        mockMvc.perform(post("/user/reset-password")
                .with(csrf())
                .flashAttr("resetPasswordEmailDto", resetPasswordEmailDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute("success", false))
                .andExpect(model().attributeExists("message"));
    }


    @Test
    public void resetPasswordConfirmationPage_givenInvalidEmail_shouldDisplayWarning() throws Exception {
        resetPasswordDto.setEmail("testuserfordbaccess@testuserfordbaccess");
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Wrong email")));
    }

    @Test
    public void resetPasswordConfirmationPage_givenNotMatchingPasswords_shouldDisplayWarning() throws Exception {
        resetPasswordDto.setPasswordForValidation("testuserfordbaccess123");
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The passwords")));
    }


    @Test
    public void resetPasswordConfirmationPage_givenNotFilledPasswords_shouldDisplayWarning() throws Exception {
        resetPasswordDto.setPasswordForValidation("");
        resetPasswordDto.setPassword("");
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please fill in")));
    }

    @Test
    public void resetPasswordConfirmationPage_givenNotFilledEmail_shouldDisplayWarning() throws Exception {
        resetPasswordDto.setEmail("");
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please fill in")));
    }

    @Test
    public void resetPasswordConfirmationPage_givenAllOk_shouldCallServiceToResetPassword() throws Exception {
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk());
        verify(service, times(1)).resetPassword(any(ResetPasswordDto.class));
    }

    @Test
    public void resetPasswordConfirmationPage_givenAllOk_shouldAddSuccessAttributeToModel() throws Exception {
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute("success", true));
    }

    @Test
    public void resetPasswordConfirmationPage_givenProcessFailed_shouldCatchHttpServerErrorException() throws Exception {
        HttpServerErrorException exception =
                new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "testException");
        doThrow(exception).when(service).resetPassword(resetPasswordDto);
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute("success", false))
                .andExpect(model().attributeExists("formErrorMessage"));

    }

    @Test
    public void resetPasswordConfirmationPage_givenProcessFailed_shouldCatchHttpClientErrorException() throws Exception {
        HttpClientErrorException exception =
                new HttpClientErrorException(HttpStatus.BAD_REQUEST, "testException");
        doThrow(exception).when(service).resetPassword(resetPasswordDto);
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute("success", false))
                .andExpect(model().attributeExists("formErrorMessage"));

    }

    @Test
    public void resetPasswordConfirmationPage_shouldAddResetLinkAttributeToModel() throws Exception {
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute("resetLink", resetPasswordDto.getResetLink()));
    }
}
