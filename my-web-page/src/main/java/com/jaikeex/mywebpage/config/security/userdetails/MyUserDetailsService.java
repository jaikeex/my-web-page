package com.jaikeex.mywebpage.config.security.userdetails;

import com.jaikeex.mywebpage.config.connection.ServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.connection.MwpServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.model.User;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.UserServiceDownException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class MyUserDetailsService implements UserDetailsService {

    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found";

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    private final ServiceRequest serviceRequest;

    @Autowired
    public MyUserDetailsService(MwpServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        try {
            String url = apiGatewayUrl + "users/username/" + username;
            ResponseEntity<User> responseEntity =
                    serviceRequest.sendGetRequest(url, User.class, UserServiceDownException.class);
            User user = responseEntity.getBody();
            return new MyUserDetails(user);
        } catch (HttpClientErrorException exception) {
            throw new UsernameNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE);
        }
    }
}
