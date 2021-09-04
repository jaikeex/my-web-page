package com.jaikeex.userservice.controller;

import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userdb")
@Slf4j
public class UserController {

    UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("")
    public User findUserByName(@RequestParam(defaultValue = "null") String username,
                               @RequestParam(defaultValue = "null") String email) {
        if (!username.equals("null")){
            log.info("Fetching user by username " + username);
            return service.findUserByName(username);
        }
        if (!email.equals("null")){
            log.info("Fetching user by email " + email);
            return service.findUserByEmail(email);
        }
        return null;
    }

    @PatchMapping("/password")
    public User updatePasswordOfUser(@RequestParam(defaultValue = "null") String username,
                                     @RequestParam(defaultValue = "-1") Integer id,
                                     @RequestParam String newPassword) {
        if (!username.equals("null")){
            log.info("Changing password of user " + username);
            return service.updatePasswordOfUser(username, newPassword);
        }
        if ( id!=-1 ) {
            log.info("Changing password of user " + id);
            return service.updatePasswordOfUser(id, newPassword);
        }
        return null;
    }

    @PatchMapping("/last-access")
    public User updateLastAccessDate(@RequestParam(defaultValue = "null") String username,
                                     @RequestParam(defaultValue = "-1") Integer id) {
        if (!username.equals("null")){
            return service.updateLastAccessDateOfUser(username);
        }
        if ( id!=-1 ) {
            return service.updateLastAccessDateOfUser(id);
        }
        return null;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        log.info("Registering new user into db: " + user);
        return service.registerUser(user);
    }
}
