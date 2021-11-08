package com.jaikeex.mywebpage.mainwebsite.service;

import com.jaikeex.mywebpage.mainwebsite.connection.MwpServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.dto.UserRegistrationFormDto;
import com.jaikeex.mywebpage.mainwebsite.dto.UserLastAccessDateDto;
import com.jaikeex.mywebpage.mainwebsite.model.User;
import com.jaikeex.mywebpage.mainwebsite.service.user.UserServiceImpl;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.UserServiceDownException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @MockBean
    MwpServiceRequest serviceRequest;

    @Autowired
    UserServiceImpl service;

    UserRegistrationFormDto userDto;
    User testUser;
    HttpEntity<User> entity;

    @BeforeEach
    public void beforeEach() {
        userDto = new UserRegistrationFormDto();
        userDto.setEmail("testEmail");
        userDto.setPassword("testPassword");
        userDto.setUsername("testUsername");
        userDto.setPasswordForValidation("testPassword");

        testUser = new User(userDto);
    }

    @Test
    public void registerUser_givenValidData_shouldCallUserServiceWithCorrectArguments(){
        service.registerNewUser(userDto);
        String API_GATEWAY_URL = "http://api-gateway:9000/";
        verify(serviceRequest, times(1)).sendPostRequest(
                API_GATEWAY_URL + "users/", testUser, UserServiceDownException.class);
    }

    @Test
    public void updateUserStatsOnLogin_givenValidData_shouldCallUserServiceWithCorrectArguments(){
        ArgumentCaptor<UserLastAccessDateDto> argument =
                ArgumentCaptor.forClass(UserLastAccessDateDto.class);
        service.updateUserStatsOnLogin("testUsername");
        verify(serviceRequest).sendPostRequest(
                anyString(), argument.capture(), any());
        assertEquals("testUsername", argument.getValue().getUsername());
    }
}