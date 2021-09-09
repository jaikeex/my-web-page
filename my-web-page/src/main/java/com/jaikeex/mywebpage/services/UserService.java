package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.dto.UserLastAccessDateDto;
import com.jaikeex.mywebpage.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Service
@Slf4j
public class UserService {



    public void registerUser (UserDto userDto, Model model) throws HttpClientErrorException {
        User user = loadDataFromDtoIntoUserObject(userDto);
        try {
            postUserToUserService(user);
            model.addAttribute("result", true);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("databaseError", true);
            model.addAttribute("databaseErrorMessage", exception.getResponseHeaders().get("databaseError"));
        }
    }

    private void postUserToUserService(User user) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        restTemplate.exchange(API_GATEWAY_URL + "users/", HttpMethod.POST, entity, User.class);
    }


    public void updateUserStatsOnLogin(String username) {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        Timestamp newLastAccessDate = new Timestamp(System.currentTimeMillis());
        UserLastAccessDateDto dto = new UserLastAccessDateDto(username, newLastAccessDate);
        System.out.println(dto);
        restTemplate.patchForObject(API_GATEWAY_URL + "users/last-access/username/" + username, dto, User.class);
    }


    private User loadDataFromDtoIntoUserObject(UserDto userDto) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setUsername(userDto.getUsername());
        userDto.encodePassword();
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);
        user.setCreationDate(now);
        user.setLastAccessDate(now);
        user.setUpdatedAt(now);
        user.setRole("ROLE_USER");
        return user;
    }


}