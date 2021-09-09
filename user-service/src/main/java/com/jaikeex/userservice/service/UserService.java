package com.jaikeex.userservice.service;

import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.repository.*;
import com.jaikeex.userservice.service.exception.InvalidResetTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Random;

@Service
@Slf4j
public class UserService {

    PasswordEncoder encoder;
    RegistrationService registrationService;
    UserRepository repository;

    @Autowired
    public UserService(UserRepository repository, RegistrationService registrationService, MyPasswordEncoder encoder) {
        this.repository = repository;
        this.registrationService = registrationService;
        this.encoder = encoder;
    }


    public User findUserByEmail(String email) {
        log.info("Fetching user by email " + email);
        return repository.findByEmail(email);
    }


    public User findUserById(Integer id) {
        log.info("Fetching user by id " + id);
        return repository.findUserById(id);
    }


    public User findUserByUsername(String username) {
        log.info("Fetching user by username " + username);
        return repository.findByUsername(username);
    }


    public User updatePasswordOfUser(String password, Integer id, String token) throws InvalidResetTokenException {
        log.info("Changing password of user " + id);
        User user = repository.findUserById(id);
        if (encoder.matches(token, user.getResetPasswordToken())) {
            repository.updatePassword(id, password);
        } else {
            throw new InvalidResetTokenException();
        }
        return repository.findUserById(id);
    }


    public User updatePasswordOfUser(String password, String email, String token) throws InvalidResetTokenException {
        log.info("Changing password of user " + email);
        User user = repository.findByEmail(email);
        if (encoder.matches(token, user.getResetPasswordToken())) {
            repository.updatePassword(email, password);
        } else {
            throw new InvalidResetTokenException();
        }
        return repository.findByEmail(email);
    }


    public User updateLastAccessDateOfUser(String username, Timestamp newLastAccessDate) {
        log.info("Updating last access date of user " + username);
        repository.updateLastAccessDate(newLastAccessDate, username);
        return repository.findByUsername(username);
    }


    public User updateLastAccessDateOfUser(Integer id, Timestamp newLastAccessDate) {
        log.info("Updating last access date of user " + id);
        repository.updateLastAccessDate(newLastAccessDate, id);
        return repository.findUserById(id);
    }




}
