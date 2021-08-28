package com.jaikeex.mywebpage.controllers;

import com.jaikeex.mywebpage.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.services.ResetPasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void shouldAddDtoToModel() throws Exception {
        mockMvc.perform(get("/user/reset-password"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("resetPasswordDto"));
    }

    @Test
    public void shouldSendConfirmationEmail() throws Exception {
        mockMvc.perform(post("/user/reset-password")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk());
        verify(service).sendConfirmationEmail(anyString());
    }

    @Test
    public void shouldResetPasswordWithValidInput() throws Exception {
        when(service.resetPassword(any(ResetPasswordDto.class))).thenReturn(true);
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("success"))
                .andExpect(content().string(containsString("Your password was changed successfully.")));
    }

    @Test
    public void shouldFailWithInvalidEmail() throws Exception {
        resetPasswordDto.setEmail("testuserfordbaccess@testuserfordbaccess");
        when(service.resetPassword(any(ResetPasswordDto.class))).thenReturn(true);
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Wrong email")));
    }

    @Test
    public void shouldFailWithNoMatchingPasswords() throws Exception {
        resetPasswordDto.setPasswordForValidation("testuserfordbaccess123");
        when(service.resetPassword(any(ResetPasswordDto.class))).thenReturn(true);
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The passwords")));
    }

    @Test
    public void shouldFailWithNoSuchUserInDb() throws Exception {
        when(service.resetPassword(any(ResetPasswordDto.class))).thenReturn(false);
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("There was an error resetting your password")));
    }

    @Test
    public void shouldFailWithBlankPasswords() throws Exception {
        resetPasswordDto.setPasswordForValidation("");
        resetPasswordDto.setPassword("");
        when(service.resetPassword(any(ResetPasswordDto.class))).thenReturn(false);
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please fill in")));
    }

    @Test
    public void shouldFailWithBlankEmail() throws Exception {
        resetPasswordDto.setEmail("");
        when(service.resetPassword(any(ResetPasswordDto.class))).thenReturn(false);
        mockMvc.perform(post("/user/reset-password-done")
                .with(csrf())
                .flashAttr("resetPasswordDto", resetPasswordDto))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please fill in")));
    }


}
