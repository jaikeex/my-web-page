package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.entity.User;
import com.jaikeex.mywebpage.jpa.UserRepository;
import com.jaikeex.mywebpage.jpa.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Timestamp;

@Service
@Transactional
public class UserRegistrationService {

    private final UserRepository repository;

    @Autowired
    public UserRegistrationService(UserRepository repository) {
        this.repository = repository;
    }

    public void registerUser (UserDto userDto, HttpServletRequest request) {
        User user = new User();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEnabled(true);
        user.setCreationDate(now);
        user.setLastAccessDate(now);
        user.setUpdatedAt(now);
        user.setRole("ROLE_USER");

        repository.save(user);
        autoLoginAfterRegistration(request, userDto.getUsername(), userDto.getPassword());
    }

    private void autoLoginAfterRegistration(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

}
