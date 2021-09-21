package com.jaikeex.mywebpage.controllers;


import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.services.UserService;
import com.jaikeex.mywebpage.utility.BindingResultErrorParser;
import com.jaikeex.mywebpage.utility.exception.RegistrationProcessFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class UserController {

    private static final String LOGIN_ENDPOINT = "/login";
    private static final String USER_AUTH_INFO_ENDPOINT = "/user/auth/info";
    private static final String SIGNUP_ENDPOINT = "/user/signup";
    private static final String LOGIN_VIEW = "user/login";
    private static final String USER_AUTH_INFO_VIEW = "user/auth/info";
    private static final String SIGNUP_VIEW = "user/signup";
    private static final String FORM_ERROR_MESSAGE_ATTRIBUTE_NAME = "formErrorMessage";

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value = LOGIN_ENDPOINT, method = RequestMethod.GET)
    public String getLoginPage() {
        return LOGIN_VIEW;
    }


    @RequestMapping(value = USER_AUTH_INFO_ENDPOINT, method = RequestMethod.GET)
    public String getUserInfoPage() {
        return USER_AUTH_INFO_VIEW;
    }


    @RequestMapping(value = SIGNUP_ENDPOINT, method = RequestMethod.GET)
    public String getRegistrationPage(Model model) {
        addUserDtoObjectToModel(model);
        return SIGNUP_VIEW;
    }


    @RequestMapping(value = SIGNUP_ENDPOINT, method = RequestMethod.POST)
    public String postRegistrationForm(Model model, @Valid UserDto userDto, BindingResult result) {
        if (isResultOk(result, model)) {
            sendUserToUserServiceForRegistration(model, userDto);
        }
        return SIGNUP_VIEW;
    }


    private boolean isResultOk(BindingResult result, Model model) {
        BindingResultErrorParser errorParser = new BindingResultErrorParser
                .Builder(result, model).messageName(FORM_ERROR_MESSAGE_ATTRIBUTE_NAME).build();
        return errorParser.isResultOk();
    }


    private void sendUserToUserServiceForRegistration(Model model, UserDto userDto) {
        try {
            userService.registerUser(userDto);
            model.addAttribute("registered", true);
        } catch (RegistrationProcessFailedException exception) {
            model.addAttribute("databaseError", true);
            model.addAttribute("databaseErrorMessage", exception.getMessage());
        }
    }


    private void addUserDtoObjectToModel(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute(userDto);
    }
}
