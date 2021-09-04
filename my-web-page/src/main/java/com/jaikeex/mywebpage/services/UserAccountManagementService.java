package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.models.ModelAttribute;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UserAccountManagementService {

    RestTemplate restTemplate;

    @Autowired
    public UserAccountManagementService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Registers the user into database if the user does not already exist.
     * @param userDto Dto containing user data necessary for registration.
     * @param model Model
     * @return User object if the process was successful, null otherwise.
     **/

    public List<ModelAttribute> registerUser (UserDto userDto, HttpServletRequest httpServletRequest) {

        userDto.encodePassword();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject userDtoJsonObject = new JSONObject(userDto);
        HttpEntity<String> request = new HttpEntity<String>(userDtoJsonObject.toString(), headers);

        ResponseEntity<ModelAttribute[]> responseEntity = restTemplate.postForEntity(
                "https://REGISTER-SERVICE/register/new-user", request, ModelAttribute[].class);
        ModelAttribute[] modelAttributes = responseEntity.getBody();
        //loginUser(httpServletRequest, userDto.getUsername(), userDto.getPassword());

        return Arrays.asList(modelAttributes);

        }


    public void updateUserStatsOnLogin(String username) {
        //restTemplate.patchForObject("https://USER-SERVICE/userdb/last-access?username=" + username, User.class);

    }
/*
    private User loadDataFromDtoIntoUserObject(UserDto userDto) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);
        user.setCreationDate(now);
        user.setLastAccessDate(now);
        user.setUpdatedAt(now);
        user.setRole("ROLE_USER");
        return user;
    }
*/
    private void loginUser(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
/*
    private boolean canBeRegisteredWithModelUpdate(User user, Model model) {
        return hasOriginalEmailWithModelUpdate(user, model) && hasOriginalUsernameWithModelUpdate(user, model);
    }

    private boolean hasOriginalUsernameWithModelUpdate(User user, Model model) {
        User responseFromDb = restTemplate.getForObject("https://USER-SERVICE/userdb?username=" + user.getUsername(), User.class);
        if (responseFromDb != null) {
            model.addAttribute("databaseError", true);
            model.addAttribute("databaseErrorMessage", "User already exists.");
            return false;
        }
        return true;
    }

    private boolean hasOriginalEmailWithModelUpdate(User user, Model model) {
        User responseFromDb = restTemplate.getForObject("https://USER-SERVICE/userdb?email=" + user.getEmail(), User.class);
        if (responseFromDb != null) {
            model.addAttribute("databaseError", true);
            model.addAttribute("databaseErrorMessage", "User with this email already exists.");
            return false;
        }
        return true;
    }*/
}