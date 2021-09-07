package com.jaikeex.userservice.controller;

import com.jaikeex.userservice.dto.UserDto;
import com.jaikeex.userservice.dto.UserLastAccessDateDto;
import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.service.RegistrationService;
import com.jaikeex.userservice.service.RegistrationServiceImpl;
import com.jaikeex.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    UserService userService;
    RegistrationService registrationService;

    @Autowired
    public UserController(UserService userService, RegistrationServiceImpl registrationService) {
        this.userService = userService;
        this.registrationService = registrationService;
    }


    @GetMapping("/id/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable Integer userId) {
        User user = userService.findUserById(userId);
        return getFindUserResponseEntity(user);
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<User> findUserByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        return getFindUserResponseEntity(user);
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<User> findUserByEmail(@PathVariable String email) {
        User user = userService.findUserByEmail(email);
        return getFindUserResponseEntity(user);
    }


    @PostMapping("/")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            user = registrationService.registerUser(user);
        } catch (Exception exception) {
            return getRegisterUserResponseEntity(exception.getMessage());
        }
        return getRegisterUserResponseEntity(user);
    }


    @PatchMapping("/id/{userId}/password/{password}")
    public ResponseEntity<User> updatePasswordOfUser(@PathVariable String password,
                                                     @PathVariable Integer userId) {
        User user = userService.updatePasswordOfUser(password, userId);
        return getPasswordUpdateResponseEntity(user);
    }


    @PatchMapping("/username/{username}/password/{password}")
    public ResponseEntity<User> updatePasswordOfUser(@PathVariable String password,
                                                     @PathVariable String username) {
        User user = userService.updatePasswordOfUser(password, username);
        return getPasswordUpdateResponseEntity(user);
    }


    @PatchMapping("/last-access/username/{username}")
    public ResponseEntity<User> updateLastAccessDateOfUser(@PathVariable String username,
                                                           @RequestBody UserLastAccessDateDto userLastAccessDateDto) {
        System.out.println(userLastAccessDateDto);
        Timestamp newLastAccessDate = userLastAccessDateDto.getLastAccessDate();
        User user = userService.updateLastAccessDateOfUser(username, newLastAccessDate);
        return getLastAccessUpdateResponseEntity(user);
    }


    @PatchMapping("/last-access/id/{id}")
    public ResponseEntity<User> updateLastAccessDateOfUser(@PathVariable Integer id, @RequestBody Timestamp newLastAccessDate) {
        User user = userService.updateLastAccessDateOfUser(id, newLastAccessDate);
        return getLastAccessUpdateResponseEntity(user);
    }


    private ResponseEntity<User> getFindUserResponseEntity(User user) {
        if (user != null) {
            return getOkUserResponseEntity(user);
        }
        else {
            return getUserNotFoundResponseEntity();
        }
    }


    private ResponseEntity<User> getUserNotFoundResponseEntity() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    private ResponseEntity<User> getRegisterUserResponseEntity(User user) {
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    private ResponseEntity<User> getRegisterUserResponseEntity(String errorMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("databaseError", errorMessage);
        return new ResponseEntity<>(headers, HttpStatus.CONFLICT);
    }

    private ResponseEntity<User> getPasswordUpdateResponseEntity(User user) {
        return getOkUserResponseEntity(user);
    }


    private ResponseEntity<User> getLastAccessUpdateResponseEntity(User user) {
        return getOkUserResponseEntity(user);
    }


    private ResponseEntity<User> getOkUserResponseEntity(User user) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
