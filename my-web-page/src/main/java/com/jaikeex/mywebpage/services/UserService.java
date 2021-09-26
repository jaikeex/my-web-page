package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.dto.UserLastAccessDateDto;
import com.jaikeex.mywebpage.model.User;
import com.jaikeex.mywebpage.restemplate.RestTemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

@Service
@Slf4j
public class UserService {

    private final RestTemplateFactory restTemplateFactory;

    public UserService(RestTemplateFactory restTemplateFactory) {
        this.restTemplateFactory = restTemplateFactory;
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
        User user = new User(userDto);
        postHttpPostRequestToUserService(user);
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
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        UserLastAccessDateDto dto = new UserLastAccessDateDto(username);
        restTemplate.patchForObject(API_GATEWAY_URL + "users/last-access/", dto, User.class);
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