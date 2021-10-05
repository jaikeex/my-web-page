package com.jaikeex.mywebpage.mainwebsite.services;

import com.jaikeex.mywebpage.mainwebsite.dto.UserDto;
import com.jaikeex.mywebpage.mainwebsite.dto.UserLastAccessDateDto;
import com.jaikeex.mywebpage.mainwebsite.model.User;
import com.jaikeex.mywebpage.resttemplate.RestTemplateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @MockBean
    RestTemplateFactory restTemplateFactory;
    @MockBean
    RestTemplate restTemplate;

    @Autowired
    UserService service;

    UserDto userDto;
    HttpEntity<User> entity;

    @BeforeEach
    public void beforeEach() {
        when(restTemplateFactory.getRestTemplate()).thenReturn(restTemplate);
        userDto = new UserDto();
        userDto.setEmail("testEmail");
        userDto.setPassword("testPassword");
        userDto.setUsername("testUsername");
        userDto.setPasswordForValidation("testPassword");

        User user = new User(userDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<>(user, headers);
    }

    @Test
    public void registerUser_givenValidData_shouldCallUserServiceWithCorrectArguments(){
        service.registerUser(userDto);
        verify(restTemplate, times(1)).exchange(
                API_GATEWAY_URL + "users/", HttpMethod.POST, entity, User.class);
    }

    @Test
    public void updateUserStatsOnLogin_givenValidData_shouldCallUserServiceWithCorrectArguments(){
        ArgumentCaptor<UserLastAccessDateDto> argument =
                ArgumentCaptor.forClass(UserLastAccessDateDto.class);
        service.updateUserStatsOnLogin("testUsername");
        verify(restTemplate).patchForObject(
                anyString(), argument.capture(), any());
        assertEquals("testUsername", argument.getValue().getUsername());
    }
}