package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MyUserDetailsService implements UserDetailsService {

    RestTemplate restTemplate;

    @Autowired
    public MyUserDetailsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = restTemplate.getForObject("https://USER-SERVICE/userdb?username=" + username, User.class);
        if (user != null) {
            return new MyUserDetails(user);
        } else {
            throw new NullPointerException("Not found " + username);
        }
    }
}
