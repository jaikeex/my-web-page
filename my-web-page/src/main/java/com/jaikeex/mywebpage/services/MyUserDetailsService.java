package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username){
        RestTemplate restTemplate = new RestTemplate();
        User user = restTemplate.getForObject(API_GATEWAY_URL + "/users/username/" + username, User.class);
        if (user != null) {
            return new MyUserDetails(user);
        } else {
            throw new NullPointerException("Not found " + username);
        }
    }
}
