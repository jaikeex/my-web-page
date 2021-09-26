package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.dto.UserLastAccessDateDto;
import com.jaikeex.mywebpage.model.User;
import com.jaikeex.mywebpage.restemplate.RestTemplateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
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

@TestComponent
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Mock
    RestTemplateFactory restTemplateFactory;
    @Mock
    RestTemplate restTemplate;

    @InjectMocks
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