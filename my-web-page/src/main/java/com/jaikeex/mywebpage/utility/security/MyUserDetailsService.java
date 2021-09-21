package com.jaikeex.mywebpage.utility.security;

import com.jaikeex.mywebpage.model.User;
import com.jaikeex.mywebpage.restemplate.RestTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

@Service
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
