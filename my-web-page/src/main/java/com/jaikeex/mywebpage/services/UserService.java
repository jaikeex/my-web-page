package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.dto.UserLastAccessDateDto;
import com.jaikeex.mywebpage.model.User;
import com.jaikeex.mywebpage.restemplate.RestTemplateFactory;
import com.jaikeex.mywebpage.utility.exception.RegistrationProcessFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

@Service
@Slf4j
public class UserService {

    private final RestTemplateFactory restTemplateFactory;

    public UserService(RestTemplateFactory restTemplateFactory) {
        this.restTemplateFactory = restTemplateFactory;
    }

    public void registerUser (UserDto userDto) throws RegistrationProcessFailedException {
        User user = new User(userDto);
        postUserToUserService(user);
    }

    public void updateUserStatsOnLogin(String username) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        UserLastAccessDateDto dto = new UserLastAccessDateDto(username);
        restTemplate.patchForObject(API_GATEWAY_URL + "users/last-access/", dto, User.class);
    }

    private void postUserToUserService(User user) throws RegistrationProcessFailedException {
        try {
            postHttpPostRequestToUserService(user);
        } catch (HttpClientErrorException exception) {
            throw new RegistrationProcessFailedException(exception.getResponseBodyAsString());
        }
    }

    private void postHttpPostRequestToUserService(User user) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        HttpEntity<User> entity = getUserHttpEntity(user);
        restTemplate.exchange(API_GATEWAY_URL + "users/", HttpMethod.POST, entity, User.class);
    }

    private HttpEntity<User> getUserHttpEntity(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(user, headers);
    }
}