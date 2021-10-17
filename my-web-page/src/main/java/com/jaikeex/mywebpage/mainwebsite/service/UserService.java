package com.jaikeex.mywebpage.mainwebsite.service;

import com.jaikeex.mywebpage.config.connection.ServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.connection.MwpServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.dto.UserDto;
import com.jaikeex.mywebpage.mainwebsite.dto.UserLastAccessDateDto;
import com.jaikeex.mywebpage.mainwebsite.model.User;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.UserServiceDownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private static final Class<? extends ServiceDownException> SERVICE_EXCEPTION_TYPE = UserServiceDownException.class;

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    private final ServiceRequest serviceRequest;

    @Autowired
    public UserService(MwpServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
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
        serviceRequest.sendPostRequest(url, user, SERVICE_EXCEPTION_TYPE);
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
        serviceRequest.sendPostRequest(url, dto, SERVICE_EXCEPTION_TYPE);
    }
}