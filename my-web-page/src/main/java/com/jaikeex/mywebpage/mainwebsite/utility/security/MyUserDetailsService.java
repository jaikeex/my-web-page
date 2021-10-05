package com.jaikeex.mywebpage.mainwebsite.utility.security;

import com.jaikeex.mywebpage.mainwebsite.model.User;
import com.jaikeex.mywebpage.resttemplate.RestTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

@Component
public class MyUserDetailsService implements UserDetailsService {

    RestTemplateFactory restTemplateFactory;

    @Autowired
    public MyUserDetailsService(RestTemplateFactory restTemplateFactory) {
        this.restTemplateFactory = restTemplateFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        try {
            User user = restTemplate.getForObject(API_GATEWAY_URL + "users/username/" + username, User.class);
            return new MyUserDetails(user);
        } catch (HttpClientErrorException exception) {
            throw new UsernameNotFoundException("Not found");
        }
    }
}
