package com.jaikeex.userservice.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jaikeex.userservice.dto.Email;
import com.jaikeex.userservice.dto.ResetPasswordDto;
import com.jaikeex.userservice.dto.UserLastAccessDateDto;
import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.repository.UserRepository;
import com.jaikeex.userservice.service.RegistrationService;
import com.jaikeex.userservice.service.RegistrationServiceImpl;
import com.jaikeex.userservice.service.ResetPasswordEmailService;
import com.jaikeex.userservice.service.UserService;
import com.jaikeex.userservice.service.exception.EmailServiceDownException;
import com.jaikeex.userservice.service.exception.InvalidResetTokenException;
import com.jaikeex.userservice.service.exception.NoSuchUserException;
import com.jaikeex.userservice.service.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;
    @MockBean
    RegistrationServiceImpl registrationService;
    @MockBean
    ResetPasswordEmailService resetPasswordEmailService;

    @Autowired
    private MockMvc mockMvc;

    User testUser = new User();
    String testUserJson;


    @BeforeEach
    public void beforeEach() throws JsonProcessingException {
        testUser.setUsername("testUsername");
        testUser.setEmail("testEmail");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        testUserJson = writer.writeValueAsString(testUser);
    }

    @Test
    public void findUserById_givenValidData_shouldReturnOk()
            throws Exception {
        when(userService.findUserById(1)).thenReturn(testUser);
        mockMvc.perform(get("/users/id/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void findUserById_givenValidData_shouldReturnUserAsJson()
            throws Exception {
        when(userService.findUserById(1)).thenReturn(testUser);
        mockMvc.perform(get("/users/id/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("username")));
    }

    @Test
    public void findUserById_givenInvalidData_shouldReturnNotFound()
            throws Exception {
        when(userService.findUserById(1)).thenReturn(null);
        mockMvc.perform(get("/users/id/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findUserByUsername_givenValidData_shouldReturnOk()
            throws Exception {
        when(userService.findUserByUsername("kuba")).thenReturn(testUser);
        mockMvc.perform(get("/users/username/kuba"))
                .andExpect(status().isOk());
    }

    @Test
    public void findUserByUsername_givenValidData_shouldReturnUserAsJson()
            throws Exception {
        when(userService.findUserByUsername("kuba")).thenReturn(testUser);
        mockMvc.perform(get("/users/username/kuba"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("username")));
    }

    @Test
    public void findUserByUsername_givenInvalidData_shouldReturnNotFound()
            throws Exception {
        when(userService.findUserByUsername("kuba")).thenReturn(null);
        mockMvc.perform(get("/users/username/kuba"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findUserByEmail_givenValidData_shouldReturnOk()
            throws Exception {
        when(userService.findUserByEmail("email")).thenReturn(testUser);
        mockMvc.perform(get("/users/email/email"))
                .andExpect(status().isOk());
    }

    @Test
    public void findUserByEmail_givenValidData_shouldReturnUserAsJson()
            throws Exception {
        when(userService.findUserByEmail("email")).thenReturn(testUser);
        mockMvc.perform(get("/users/email/email"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("username")));
    }

    @Test
    public void findUserByEmail_givenInvalidData_shouldReturnNotFound()
            throws Exception {
        when(userService.findUserByEmail("email")).thenReturn(null);
        mockMvc.perform(get("/users/email/email"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void registerUser_givenValidForm_shouldReturnCreated()
            throws Exception {
        when(registrationService.registerUser(any(User.class))).thenReturn(testUser);
        mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUserJson))
                .andExpect(status().isCreated());

    }

    @Test
    public void registerUser_givenValidForm_shouldReturnUserAsJson()
            throws Exception {
        when(registrationService.registerUser(any(User.class))).thenReturn(testUser);
        mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUserJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("username")));

    }

    @Test
    public void registerUser_givenInvalidForm_shouldReturnConflict()
            throws Exception {
        when(registrationService.registerUser(any(User.class)))
                .thenThrow(UserAlreadyExistsException.class);
        mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUserJson))
                .andExpect(status().isConflict());
    }

    @Test
    public void registerUser_givenValidForm_shouldCallServiceToSaveUser()
            throws Exception {
        when(registrationService.registerUser(any(User.class))).thenReturn(testUser);
        mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUserJson));
        verify(registrationService, times(1))
                .registerUser(any(User.class));
    }

    @Test
    public void registerUser_givenValidForm_SavedUserShouldIncludeFormData()
            throws Exception {
        when(registrationService.registerUser(any(User.class))).thenReturn(testUser);
        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUserJson));
        verify(registrationService).registerUser(argument.capture());
        assertEquals("testUsername", argument.getValue().getUsername());
        assertEquals("testEmail", argument.getValue().getEmail());
    }

    @Test
    public void updatePasswordOfUserByID_givenValidArguments_shouldReturnOk()
            throws Exception {
        when(userService.updatePasswordOfUser(anyInt(), any(ResetPasswordDto.class)))
                .thenReturn(testUser);
        mockMvc.perform(patch("/users/password/id/1/token/resetToken")
                .content("newPassword"))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePasswordOfUserByEmail_givenValidArguments_shouldReturnOk()
            throws Exception {
        when(userService.updatePasswordOfUser(anyString(), any(ResetPasswordDto.class)))
                .thenReturn(testUser);
        mockMvc.perform(patch("/users/password/email/email/token/resetToken")
                .content("newPassword"))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePasswordOfUserByID_givenNoSuchUser_shouldReturnConflict()
            throws Exception {
        when(userService.updatePasswordOfUser(anyInt(), any(ResetPasswordDto.class)))
                .thenThrow(NoSuchUserException.class);
        mockMvc.perform(patch("/users/password/id/1/token/resetToken")
                .content("newPassword"))
                .andExpect(status().isConflict());
    }

    @Test
    public void updatePasswordOfUserByEmail_givenNoSuchUser_shouldReturnConflict()
            throws Exception {
        when(userService.updatePasswordOfUser(anyString(), any(ResetPasswordDto.class)))
                .thenThrow(NoSuchUserException.class);
        mockMvc.perform(patch("/users/password/email/email/token/resetToken")
                .content("newPassword"))
                .andExpect(status().isConflict());
    }

    @Test
    public void updatePasswordOfUserByID_givenValidArguments_shouldPassCorrectDataToDatabase()
            throws Exception {
        when(userService.updatePasswordOfUser(anyInt(), any(ResetPasswordDto.class)))
                .thenReturn(testUser);
        ArgumentCaptor<ResetPasswordDto> argument =
                ArgumentCaptor.forClass(ResetPasswordDto.class);
        mockMvc.perform(patch("/users/password/id/1/token/resetToken")
                .content("newPassword"));
        verify(userService).updatePasswordOfUser(anyInt(), argument.capture());
        assertEquals("newPassword", argument.getValue().getPassword());
        assertEquals("resetToken", argument.getValue().getResetToken());
    }

    @Test
    public void updatePasswordOfUserByEmail_givenValidArguments_shouldPassCorrectDataToDatabase()
            throws Exception {
        when(userService.updatePasswordOfUser(anyString(), any(ResetPasswordDto.class)))
                .thenReturn(testUser);
        ArgumentCaptor<ResetPasswordDto> argument =
                ArgumentCaptor.forClass(ResetPasswordDto.class);
        mockMvc.perform(patch("/users/password/email/email/token/resetToken")
                .content("newPassword"));
        verify(userService).updatePasswordOfUser(anyString(), argument.capture());
        assertEquals("newPassword", argument.getValue().getPassword());
        assertEquals("resetToken", argument.getValue().getResetToken());
    }

    @Test
    public void updateLastAccessDateOfUser_shouldUpdateLastAccessDateOfUser()
            throws Exception {
        UserLastAccessDateDto dto =
                new UserLastAccessDateDto("testUsername", new Timestamp(0));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String testDtoJson = writer.writeValueAsString(dto);
        when(userService.updateLastAccessDateOfUser(anyString(), any(Timestamp.class)))
                .thenReturn(testUser);
        mockMvc.perform(patch("/users/last-access/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testDtoJson));
        verify(userService, times(1))
                .updateLastAccessDateOfUser(anyString(), any(Timestamp.class));
    }

    @Test
    public void updateLastAccessDateOfUser_givenValidData_shouldReturnOk()
            throws Exception {
        UserLastAccessDateDto dto =
                new UserLastAccessDateDto("testUsername", new Timestamp(0));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String testDtoJson = writer.writeValueAsString(dto);
        when(userService.updateLastAccessDateOfUser(anyString(), any(Timestamp.class)))
                .thenReturn(testUser);
        mockMvc.perform(patch("/users/last-access/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    public void sendResetPasswordConfirmationEmail_shouldCallService () throws Exception {
        mockMvc.perform(get("/users/reset-password/email/email"));
        verify(resetPasswordEmailService, times(1))
                .sendResetPasswordConfirmationEmail("email");
    }

    @Test
    public void sendResetPasswordConfirmationEmail_givenAllOk_shouldReturnOk () throws Exception {
        mockMvc.perform(get("/users/reset-password/email/email"))
        .andExpect(status().isOk());
    }

    @Test
    public void sendResetPasswordConfirmationEmail_givenError_shouldThrowException () throws Exception {
        doThrow(EmailServiceDownException.class)
                .when(resetPasswordEmailService).sendResetPasswordConfirmationEmail(anyString());
        mockMvc.perform(get("/users/reset-password/email/email"))
                .andExpect(status().isInternalServerError());
    }
}



















