package com.jaikeex.registerservice.service;

import com.jaikeex.registerservice.VO.User;
import com.jaikeex.registerservice.dto.ModelAttributeDto;
import com.jaikeex.registerservice.dto.UserDto;
import com.jaikeex.registerservice.models.ModelAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class RegisterService {

    RestTemplate restTemplate;
    private List<ModelAttribute> attributes = new LinkedList<>();

    @Autowired
    public RegisterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Registers the user into database if the user does not already exist.
     * @param userDto Dto containing user data necessary for registration.
     */
    public List<ModelAttribute> registerUser(UserDto userDto) {
        User user = loadDataFromDtoIntoUserObject(userDto);
        if (canBeRegisteredWithModelUpdate(user)) {
            restTemplate.postForObject("https://USER-SERVICE/userdb/register", user, User.class);
            attributes.add(new ModelAttribute("result", true));
        }
        for (Object attribute : attributes) {
            log.info(attribute.toString());
        }
        return attributes;
    }

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

    private boolean canBeRegisteredWithModelUpdate(User user) {
        return hasOriginalEmailWithModelUpdate(user) && hasOriginalUsernameWithModelUpdate(user);
    }

    private boolean hasOriginalUsernameWithModelUpdate(User user) {
        User responseFromDb = restTemplate.getForObject("https://USER-SERVICE/userdb?username=" + user.getUsername(), User.class);
        if (responseFromDb != null) {
            attributes.add(new ModelAttribute("databaseError", true));
            attributes.add(new ModelAttribute("databaseErrorMessage", "User already exists."));
            return false;
        }
        return true;
    }

    private boolean hasOriginalEmailWithModelUpdate(User user) {
        User responseFromDb = restTemplate.getForObject("https://USER-SERVICE/userdb?email=" + user.getEmail(), User.class);
        if (responseFromDb != null) {
            attributes.add(new ModelAttribute("databaseError", true));
            attributes.add(new ModelAttribute("databaseErrorMessage", "User with this email already exists."));
            return false;
        }
        return true;
    }

}
