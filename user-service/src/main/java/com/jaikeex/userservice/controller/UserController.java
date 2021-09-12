package com.jaikeex.userservice.controller;

import com.jaikeex.userservice.dto.ResetPasswordDto;
import com.jaikeex.userservice.dto.UserLastAccessDateDto;
import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.service.RegistrationService;
import com.jaikeex.userservice.service.RegistrationServiceImpl;
import com.jaikeex.userservice.service.ResetPasswordEmailService;
import com.jaikeex.userservice.service.UserService;
import com.jaikeex.userservice.service.exception.InvalidResetTokenException;
import com.jaikeex.userservice.service.exception.NoSuchUserException;
import com.jaikeex.userservice.service.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    UserService userService;
    RegistrationService registrationService;
    ResetPasswordEmailService resetPasswordEmailService;

    @Autowired
    public UserController(UserService userService, RegistrationServiceImpl registrationService, ResetPasswordEmailService resetPasswordEmailService) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.resetPasswordEmailService = resetPasswordEmailService;
    }


    @GetMapping("/id/{userId}")
    public ResponseEntity<Object> findUserById(@PathVariable Integer userId) {
        log.debug("entering /users/id endpoint");
        User user = userService.findUserById(userId);
        return getFindUserResponseEntity(user);
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<Object> findUserByUsername(@PathVariable String username) {
        log.debug("entering /users/username endpoint");
        User user = userService.findUserByUsername(username);
        return getFindUserResponseEntity(user);
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<Object> findUserByEmail(@PathVariable String email) {
        log.debug("entering /users/email/ endpoint");
        User user = userService.findUserByEmail(email);
        return getFindUserResponseEntity(user);
    }


    @PostMapping("/")
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        log.debug("entering /users/ endpoint");
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
        log.debug("entering /users/password/id/-/token/ endpoint");
        ResetPasswordDto resetPasswordDto = new ResetPasswordDto(password, token);
        try {
            User user = userService.updatePasswordOfUser(userId, resetPasswordDto);
            return getPasswordUpdateResponseEntity(user);
        } catch (InvalidResetTokenException | NoSuchUserException exception) {
            return getInvalidResetTokenResponseEntity(exception.getMessage());
        }
    }


    @PatchMapping("/password/email/{email}/token/{token}")
    public ResponseEntity<Object> updatePasswordOfUser(@PathVariable String email,
                                                     @PathVariable String token,
                                                     @RequestBody String password) {
        log.debug("entering /users/password/email/-/token/ endpoint");
        ResetPasswordDto resetPasswordDto = new ResetPasswordDto(password, token);
        try {
            User user = userService.updatePasswordOfUser(email, resetPasswordDto);
            return getPasswordUpdateResponseEntity(user);
        } catch (InvalidResetTokenException | NoSuchUserException exception) {
            return getInvalidResetTokenResponseEntity(exception.getMessage());
        }
    }

    @PatchMapping("/last-access/")
    public ResponseEntity<Object> updateLastAccessDateOfUser(@RequestBody UserLastAccessDateDto userLastAccessDateDto) {
        log.debug("entering /users/last-access/ endpoint");
        Timestamp newLastAccessDate = userLastAccessDateDto.getLastAccessDate();
        User user = userService.updateLastAccessDateOfUser(userLastAccessDateDto.getUsername(), newLastAccessDate);
        return getLastAccessUpdateResponseEntity(user);
    }


    @GetMapping("/reset-password/email/{email}")
    public ResponseEntity<Object> sendResetPasswordConfirmationEmail(@PathVariable String email) {
        log.debug("entering /users/reset-password/email/ endpoint");
        try {
            resetPasswordEmailService.sendResetPasswordConfirmationEmail(email);
            log.info("Reset password confirmation email sent successfully to {}", email);
            return getOkUserResponseEntity(null);
        } catch (Exception exception) {
            log.warn("There was an error when sending reset password confirmation email");
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
