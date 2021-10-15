package com.jaikeex.mywebpage.mainwebsite.controller;


import com.jaikeex.mywebpage.mainwebsite.dto.UserDto;
import com.jaikeex.mywebpage.mainwebsite.service.UserService;
import com.jaikeex.mywebpage.mainwebsite.utility.BindingResultErrorParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.Valid;

@Controller
public class UserController {

    private static final String LOGIN_VIEW = "user/login";
    private static final String USER_AUTH_INFO_VIEW = "user/auth/info";
    private static final String SIGNUP_VIEW = "user/signup";
    private static final String FORM_ERROR_MESSAGE_ATTRIBUTE_NAME = "formErrorMessage";
    private static final String REGISTRATION_STATUS_ATTRIBUTE_NAME = "registered";
    private static final String DATABASE_ERROR_STATUS_ATTRIBUTE_NAME = "databaseError";
    private static final String DATABASE_ERROR_MESSAGE_ATTRIBUTE_NAME = "databaseErrorMessage";

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/login")
    public String getLoginPage() {
        return LOGIN_VIEW;
    }

    @GetMapping(value = "/user/auth/info")
    public String getUserInfoPage() {
        return USER_AUTH_INFO_VIEW;
    }

    @GetMapping(value = "/user/signup")
    public String getRegistrationPage(Model model) {
        addUserDtoObjectToModel(model);
        return SIGNUP_VIEW;
    }

    @PostMapping(value = "/user/signup")
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
        userService.registerUser(userDto);
        appendModelWithRegistrationSuccessAttributes(model);
    }

    private void appendModelWithRegistrationSuccessAttributes(Model model) {
        model.addAttribute(REGISTRATION_STATUS_ATTRIBUTE_NAME, true);
    }

    private void appendModelWithRegistrationFailedAttributes(Model model, Exception exception) {
        model.addAttribute(DATABASE_ERROR_STATUS_ATTRIBUTE_NAME, true);
        model.addAttribute(DATABASE_ERROR_MESSAGE_ATTRIBUTE_NAME, exception.getMessage());
    }

    private void addUserDtoObjectToModel(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute(userDto);
    }

    @ExceptionHandler({HttpServerErrorException.class, HttpClientErrorException.class})
    public String userServiceError(Model model, HttpStatusCodeException exception) {
        addUserDtoObjectToModel(model);
        appendModelWithRegistrationFailedAttributes(model, exception);
        return SIGNUP_VIEW;
    }
}