package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.entity.User;
import com.jaikeex.mywebpage.jpa.UserRepository;
import com.jaikeex.mywebpage.services.security.MyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Timestamp;

@Service
@Transactional
public class UserAccountManagementService {

    private final UserRepository repository;

    @Autowired
    public UserAccountManagementService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean registerUser (UserDto userDto, HttpServletRequest request, Model model) {
        User user = loadDataFromDtoIntoUserObject(userDto);
        if (canBeRegisteredWithModelUpdate(userDto, model)) {
            repository.save(user);
            loginUser(request, userDto.getUsername(), userDto.getPassword());
            return true;
        }
        return false;
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
        if (repository.findByEmail(userDto.getEmail()) != null && !userDto.getEmail().equals("")) {
            model.addAttribute("databaseError", true);
            model.addAttribute("databaseErrorMessage", "User with this email already exists.");
            return false;
        }
        return true;
    }



}