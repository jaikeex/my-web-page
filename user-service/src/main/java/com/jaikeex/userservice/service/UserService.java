package com.jaikeex.userservice.service;

import com.jaikeex.userservice.dto.UserDto;
import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class UserService {

    RegistrationService registrationService;
    UserRepository repository;

    @Autowired
    public UserService(UserRepository repository, RegistrationService registrationService) {
        this.repository = repository;
        this.registrationService = registrationService;
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


    public User updatePasswordOfUser(String password, Integer id) {
        log.info("Changing password of user " + id);
        repository.updatePassword(id, password);
        return repository.findUserById(id);
    }


    public User updatePasswordOfUser(String password, String username) {
        log.info("Changing password of user " + username);
        repository.updatePassword(username, password);
        return repository.findByUsername(username);
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
