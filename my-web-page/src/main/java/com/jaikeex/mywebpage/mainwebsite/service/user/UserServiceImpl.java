package com.jaikeex.mywebpage.mainwebsite.service.user;

import com.jaikeex.mywebpage.config.connection.ServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.connection.MwpServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.dto.UserLastAccessDateDto;
import com.jaikeex.mywebpage.mainwebsite.dto.UserRegistrationFormDto;
import com.jaikeex.mywebpage.mainwebsite.model.User;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.UserServiceDownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final Class<? extends ServiceDownException> SERVICE_EXCEPTION_TYPE = UserServiceDownException.class;

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    private final ServiceRequest serviceRequest;

    @Autowired
    public UserServiceImpl(MwpServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    @Override
    public void registerNewUser(UserRegistrationFormDto userDto){
        String url = apiGatewayUrl + "users/";
        User user = new User(userDto);
        serviceRequest.sendPostRequest(url, user, SERVICE_EXCEPTION_TYPE);
    }

    @Override
    public void updateUserStatsOnLogin(String username) {
        String url = apiGatewayUrl + "users/last-access/";
        UserLastAccessDateDto dto = new UserLastAccessDateDto(username);
        serviceRequest.sendPostRequest(url, dto, SERVICE_EXCEPTION_TYPE);
    }
}