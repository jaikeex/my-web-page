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
    private final MyPasswordEncoder encoder;

    @Autowired
    public UserAccountManagementService(UserRepository repository, MyPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public void registerUser (UserDto userDto, HttpServletRequest request, Model model) {
        User user = new User();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);
        user.setCreationDate(now);
        user.setLastAccessDate(now);
        user.setUpdatedAt(now);
        user.setRole("ROLE_USER");
        if (canBeRegistered(userDto, model)) {
            repository.save(user);
            loginUser(request, userDto.getUsername(), userDto.getPassword());
        }
    }

    private void loginUser(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    public void changePassword (UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        String encodedNewPassword = encoder.encode(userDto.getPassword());
        repository.updatePassword(encodedNewPassword, currentUserName);
    }

    private boolean canBeRegistered(UserDto userDto, Model model) {
        if (repository.findByUsername(userDto.getUsername()) != null) {
            model.addAttribute("databaseError", true);
            model.addAttribute("databaseErrorMessage", "User already exists.");
            return false;
        }
        if (!userDto.getEmail().equals("")) {
            if (repository.findByEmail(userDto.getEmail()) != null) {
                model.addAttribute("databaseError", true);
                model.addAttribute("databaseErrorMessage", "User with this email already exists.");
                return false;
            }
        }
        return true;
    }

}