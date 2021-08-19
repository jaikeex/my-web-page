package com.jaikeex.mywebpage.controllers;


import com.jaikeex.mywebpage.jpa.dto.UserDto;
import com.jaikeex.mywebpage.services.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class UserController {

    UserRegistrationService userRegistrationService;

    @Autowired
    public UserController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @RequestMapping(value = "user/info")
    public String info(Model model) {
        return "user/info";
    }

    @GetMapping(value = "user/signup")
    public String signup(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute(userDto);
        return "user/signup";
    }

    @PostMapping(value = "user/signup")
    @Validated
    public String registerUser(Model model, @Valid UserDto userDto, BindingResult result, HttpServletRequest request) {
        model.addAttribute(userDto);
        if (result.hasErrors()) {
            model.addAttribute("passwordMatchErrorMessage", "The passwords didn't match.");
        } else {
            userRegistrationService.registerUser(userDto, request);
        }
        return "user/signup";
    }
}
