package com.jaikeex.registerservice.service;

import com.jaikeex.registerservice.jsonvo.User;
import com.jaikeex.registerservice.dto.UserDto;
import com.jaikeex.registerservice.models.ModelAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class RegisterService {

    RestTemplate restTemplate;
    private final List<ModelAttribute> modelAttributes = new LinkedList<>();

    @Autowired
    public RegisterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ModelAttribute> registerUser(UserDto userDto) {
        User user = loadDataFromDtoIntoUserObject(userDto);
        if (canBeRegisteredWithModelUpdate(user)) {
            restTemplate.postForObject("https://USER-SERVICE/userdb/register", user, User.class);
            modelAttributes.add(new ModelAttribute("result", true));
        }
        for (Object attribute : modelAttributes) {
            log.info(attribute.toString());
        }
        return modelAttributes;
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
            modelAttributes.add(new ModelAttribute("databaseError", true));
            modelAttributes.add(new ModelAttribute("databaseErrorMessage", "User already exists."));
            return false;
        }
        return true;
    }

    private boolean hasOriginalEmailWithModelUpdate(User user) {
        ResponseEntity<User> responseEntity = restTemplate.getForEntity("https://USER-SERVICE/userdb?email=" + user.getEmail(), User.class);
        System.out.println(responseEntity.getStatusCode());
        User responseFromDb = restTemplate.getForObject("https://USER-SERVICE/userdb?email=" + user.getEmail(), User.class);
        if (responseFromDb != null) {
            modelAttributes.add(new ModelAttribute("databaseError", true));
            modelAttributes.add(new ModelAttribute("databaseErrorMessage", "User with this email already exists."));
            return false;
        }
        return true;
    }



}
