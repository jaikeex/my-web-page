package com.jaikeex.mywebpage.mainwebsite.utility.security;

import com.jaikeex.mywebpage.mainwebsite.model.User;
import com.jaikeex.mywebpage.config.resttemplate.RestTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    RestTemplateFactory restTemplateFactory;

    @Autowired
    public MyUserDetailsService(RestTemplateFactory restTemplateFactory) {
        this.restTemplateFactory = restTemplateFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        try {
            String url = apiGatewayUrl + "users/username/" + username;
            User user = restTemplate.getForObject(url, User.class);
            return new MyUserDetails(user);
        } catch (HttpClientErrorException exception) {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
