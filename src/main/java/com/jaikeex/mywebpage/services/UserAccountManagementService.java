package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.entity.User;
import com.jaikeex.mywebpage.jpa.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Timestamp;

@Service
@Transactional
@Slf4j
public class UserAccountManagementService {

    private final UserRepository repository;

    @Autowired
    public UserAccountManagementService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Registers the user into database if the user does not already exist.
     * @param userDto Dto containing user data necessary for registration.
     * @param request Http request.
     * @param model Model
     * @return User object if the process was successful, null otherwise.
     */
    public User registerUser (UserDto userDto, HttpServletRequest request, Model model) {

        if (canBeRegisteredWithModelUpdate(userDto, model)) {
            User user = loadDataFromDtoIntoUserObject(userDto);
            repository.save(user);
            loginUser(request, userDto.getUsername(), userDto.getPassword());
            log.info("Registered user: " + user);
            return user;
        }
        return null;
    }

    public void updateUserStatsOnLogin(String username) {
        Timestamp newLastAccessDate = new Timestamp(System.currentTimeMillis());
        repository.updateLastAccessDate(newLastAccessDate, username);
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

    private void loginUser(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    private boolean canBeRegisteredWithModelUpdate(UserDto userDto, Model model) {
        return hasOriginalEmailWithModelUpdate(userDto, model) && hasOriginalUsernameWithModelUpdate(userDto, model);
    }

    private boolean hasOriginalUsernameWithModelUpdate(UserDto userDto, Model model) {
        if (repository.findByUsername(userDto.getUsername()) != null) {
            model.addAttribute("databaseError", true);
            model.addAttribute("databaseErrorMessage", "User already exists.");
            return false;
        }
        return true;
    }

    private boolean hasOriginalEmailWithModelUpdate(UserDto userDto, Model model) {
        if (repository.findByEmail(userDto.getEmail()) != null) {
            model.addAttribute("databaseError", true);
            model.addAttribute("databaseErrorMessage", "User with this email already exists.");
            return false;
        }
        return true;
    }
}