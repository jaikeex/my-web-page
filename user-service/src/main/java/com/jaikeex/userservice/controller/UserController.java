package com.jaikeex.userservice.controller;

import com.jaikeex.userservice.dto.UserLastAccessDateDto;
import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.service.RegistrationService;
import com.jaikeex.userservice.service.RegistrationServiceImpl;
import com.jaikeex.userservice.service.ResetPasswordService;
import com.jaikeex.userservice.service.UserService;
import com.jaikeex.userservice.service.exception.InvalidResetTokenException;
import com.jaikeex.userservice.service.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.sql.Timestamp;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    UserService userService;
    RegistrationService registrationService;
    ResetPasswordService resetPasswordService;

    @Autowired
    public UserController(UserService userService, RegistrationServiceImpl registrationService, ResetPasswordService resetPasswordService) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.resetPasswordService = resetPasswordService;
    }


    @GetMapping("/id/{userId}")
    public ResponseEntity<Object> findUserById(@PathVariable Integer userId) {
        User user = userService.findUserById(userId);
        return getFindUserResponseEntity(user);
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<Object> findUserByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        return getFindUserResponseEntity(user);
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<Object> findUserByEmail(@PathVariable String email) {
        User user = userService.findUserByEmail(email);
        return getFindUserResponseEntity(user);
    }


    @PostMapping("/")
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        try {
            user = registrationService.registerUser(user);
            return getRegisterUserResponseEntity(user);
        } catch (UserAlreadyExistsException exception) {
            return getRegisterUserResponseEntity(exception.getMessage());
        }
    }


    @PatchMapping("/password/id/{userId}/token/{token}")
    public ResponseEntity<Object> updatePasswordOfUser(@PathVariable String token,
                                                       @PathVariable Integer userId,
                                                       @RequestBody String password) {
        try {
            User user = userService.updatePasswordOfUser(password, userId, token);
            return getPasswordUpdateResponseEntity(user);
        } catch (InvalidResetTokenException exception) {
            return getInvalidResetTokenResponseEntity(exception.getMessage());
        }
    }


    @PatchMapping("/password/email/{email}/token/{token}")
    public ResponseEntity<Object> updatePasswordOfUser(@PathVariable String email,
                                                     @PathVariable String token,
                                                     @RequestBody String password) {
        try {
            User user = userService.updatePasswordOfUser(password, email, token);
            return getPasswordUpdateResponseEntity(user);
        } catch (InvalidResetTokenException exception) {
            return getInvalidResetTokenResponseEntity(exception.getMessage());
        }
    }

    @PatchMapping("/last-access/")
    public ResponseEntity<Object> updateLastAccessDateOfUser(@RequestBody UserLastAccessDateDto userLastAccessDateDto) {
        Timestamp newLastAccessDate = userLastAccessDateDto.getLastAccessDate();
        User user = userService.updateLastAccessDateOfUser(userLastAccessDateDto.getUsername(), newLastAccessDate);
        return getLastAccessUpdateResponseEntity(user);
    }


    @GetMapping("/reset-password/email/{email}")
    public ResponseEntity<Object> sendResetPasswordConfirmationEmail(@PathVariable String email) {
        try {
            resetPasswordService.sendResetPasswordConfirmationEmail(email);
            return getOkUserResponseEntity(null);
        } catch (Exception exception) {
            return getEmailNotSendResponseEntity(exception.getMessage());
        }
    }


    private ResponseEntity<Object> getEmailNotSendResponseEntity(String errorMessage) {
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private ResponseEntity<Object> getFindUserResponseEntity(User user) {
        if (user != null) {
            return getOkUserResponseEntity(user);
        }
        else {
            return getUserNotFoundResponseEntity();
        }
    }


    private ResponseEntity<Object> getUserNotFoundResponseEntity() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    private ResponseEntity<Object> getRegisterUserResponseEntity(User user) {
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    private ResponseEntity<Object> getRegisterUserResponseEntity(String errorMessage) {
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    private ResponseEntity<Object> getPasswordUpdateResponseEntity(User user) {
        return getOkUserResponseEntity(user);
    }


    private ResponseEntity<Object> getLastAccessUpdateResponseEntity(User user) {
        return getOkUserResponseEntity(user);
    }


    private ResponseEntity<Object> getOkUserResponseEntity(Object user) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private ResponseEntity<Object> getInvalidResetTokenResponseEntity(String errorMessage) {
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }
}
