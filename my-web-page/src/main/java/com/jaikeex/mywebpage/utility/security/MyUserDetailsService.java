package com.jaikeex.mywebpage.utility.security;

import com.jaikeex.mywebpage.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username){
        RestTemplate restTemplate = new RestTemplate();
        try {
            User user = restTemplate.getForObject(API_GATEWAY_URL + "users/username/" + username, User.class);
            return new MyUserDetails(user);
        } catch (HttpClientErrorException exception) {
            throw new UsernameNotFoundException("Not found");
        }
    }
}
