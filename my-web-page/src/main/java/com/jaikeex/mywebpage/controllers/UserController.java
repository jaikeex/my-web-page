package com.jaikeex.mywebpage.controllers;


import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/login")
    public String about () {
        return "/user/login";
    }

    @RequestMapping(value = "/auth/info")
    public String info() {
        return "user/auth/info";
    }

    @GetMapping(value = "/signup")
    public String signup(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute(userDto);
        return "user/signup";
    }

    @PostMapping(value = "/signup")
    public String registerUser(Model model, @Valid UserDto userDto, BindingResult result) {
        parseSignupFormErrors(result, model);
        if (!result.hasErrors()) {
            userService.registerUser(userDto, model);
        }
        return "user/signup";
    }

    private void parseSignupFormErrors(BindingResult result, Model model) {
        for (ObjectError error : result.getAllErrors()) {
            if (error.toString().contains("Blank")) {
                model.addAttribute("passwordMatchErrorMessage", "Please fill in all the fields!");
            }
            if (error.toString().contains("Pattern.email")) {
                model.addAttribute("wrongEmailMessage", "Wrong email!");
            }
            if (error.toString().contains("PasswordMatches")) {
                model.addAttribute("passwordMatchErrorMessage", "The passwords didn't match!");
            }
        }
    }
}
