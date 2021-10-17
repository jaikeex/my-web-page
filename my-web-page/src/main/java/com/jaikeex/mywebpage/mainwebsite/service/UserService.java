package com.jaikeex.mywebpage.mainwebsite.service;

import com.jaikeex.mywebpage.config.circuitbreaker.FallbackHandler;
import com.jaikeex.mywebpage.config.circuitbreaker.qualifier.CircuitBreakerName;
import com.jaikeex.mywebpage.mainwebsite.controller.fallback.MwpFallbackHandler;
import com.jaikeex.mywebpage.mainwebsite.dto.UserDto;
import com.jaikeex.mywebpage.mainwebsite.dto.UserLastAccessDateDto;
import com.jaikeex.mywebpage.mainwebsite.model.User;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.UserServiceDownException;
import com.jaikeex.mywebpage.resttemplate.RestTemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserService {

    private static final String USER_SERVICE_CIRCUIT_BREAKER_NAME = "USER_SERVICE_CB";
    private static final String REGISTER_USER_ERROR_MESSAGE = "There was an error registering the new user.";
    private static final String UPDATE_USER_DATA_ERROR_MESSAGE = "There was an error updating the user data.";

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    private final RestTemplate restTemplate;
    private final CircuitBreaker circuitBreaker;
    private final FallbackHandler fallbackHandler;

    @Autowired
    public UserService(RestTemplateFactory restTemplateFactory,
                       @CircuitBreakerName(USER_SERVICE_CIRCUIT_BREAKER_NAME) CircuitBreaker circuitBreaker,
                       MwpFallbackHandler fallbackHandler) {
        this.restTemplate = restTemplateFactory.getRestTemplate();
        this.circuitBreaker = circuitBreaker;
        this.fallbackHandler = fallbackHandler;
    }

    /**Sends the new user's data to the user service to save them into the
     * database.
     * @param userDto Data transfer object with user data necessary to complete
     *                the registration process.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws org.springframework.web.client.HttpServerErrorException
     *          Whenever a 5xx http status code gets returned.
     */
    public void registerUser (UserDto userDto){
        String url = apiGatewayUrl + "users/";
        User user = new User(userDto);
        sendPostRequest(url, user, REGISTER_USER_ERROR_MESSAGE);
    }

    /**Performs all the updates necessary when the user with a given username
     * logs in.
     * @param username Name of the user that just logged in.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws org.springframework.web.client.HttpServerErrorException
     *          Whenever a 5xx http status code gets returned.
     */
    public void updateUserStatsOnLogin(String username) {
        String url = apiGatewayUrl + "users/last-access/";
        UserLastAccessDateDto dto = new UserLastAccessDateDto(username);
        sendPostRequest(url, dto, UPDATE_USER_DATA_ERROR_MESSAGE);
    }

    private void sendPostRequest(String url, Object body, String fallbackMessage) {
        circuitBreaker.run(() -> restTemplate.postForEntity(url, body, User.class),
                throwable -> fallbackHandler.throwFallbackException(
                        fallbackMessage, throwable, UserServiceDownException.class));
    }
}