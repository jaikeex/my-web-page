package com.jaikeex.mywebpage.controllers;


import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.services.UserManagementService;
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

    UserManagementService userManagementService;

    @Autowired
    public UserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @RequestMapping(value = "user/auth/info")
    public String info(Model model) {
        return "user/auth/info";
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
            userManagementService.registerUser(userDto, request);
        }
        return "user/signup";
    }


    @GetMapping(value = "user/auth/change-password")
    @Validated
    public String changePasswordForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute(userDto);
        return "user/auth/changepassword";
    }


    @PostMapping(value = "user/auth/change-password")
    @Validated
    public String changePassword(Model model, @Valid UserDto userDto, BindingResult result) {
        model.addAttribute(userDto);
        if (result.hasErrors()) {
            model.addAttribute("passwordMatchErrorMessage", "The passwords didn't match.");
            return "user/auth/changepassword";
        } else {
            userManagementService.changePassword(userDto);
            model.addAttribute("passwordChanged", true);
            return "user/auth/info";
        }
    }
}
