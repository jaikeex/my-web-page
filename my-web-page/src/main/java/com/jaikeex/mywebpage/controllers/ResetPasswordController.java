package com.jaikeex.mywebpage.controllers;


import com.jaikeex.mywebpage.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.dto.ResetPasswordEmailDto;
import com.jaikeex.mywebpage.services.ResetPasswordService;
import com.jaikeex.mywebpage.utility.BindingResultErrorParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;

@Controller
public class ResetPasswordController {

    private static final String FORM_ERROR_MESSAGE_ATTRIBUTE_NAME = "formErrorMessage";
    private static final String SUCCESS_MODEL_ATTRIBUTE_NAME = "success";
    private static final String SENDING_STATUS_MODEL_ATTRIBUTE_NAME = "message";
    private static final String RESET_LINK_MODEL_ATTRIBUTE_NAME = "resetLink";
    private static final String RESET_PASSWORD_ENDPOINT = "/user/reset-password";
    private static final String RESET_PASSWORD_DONE_ENDPOINT = "/user/reset-password-done";
    private static final String RESET_PASSWORD_VIEW = "user/reset-password";
    private static final String RESET_PASSWORD_DONE_VIEW = "user/reset-password-done";


    ResetPasswordService resetPasswordService;

    @Autowired
    public ResetPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @GetMapping(RESET_PASSWORD_ENDPOINT)
    public String getResetPasswordPage(Model model) {
        addDtoObjectsToModel(model);
        return RESET_PASSWORD_VIEW;
    }


    @PostMapping(RESET_PASSWORD_ENDPOINT)
    public String postResetPasswordForm(@Valid ResetPasswordEmailDto resetPasswordEmailDto, BindingResult result, Model model) {
        if (isResultOk(result, model)) {
            passEmailDataToResetPasswordService(resetPasswordEmailDto, model);
        }
        return RESET_PASSWORD_VIEW;
    }


    @PostMapping(RESET_PASSWORD_DONE_ENDPOINT)
    public String resetPasswordConfirmationPage(Model model, @Valid ResetPasswordDto resetPasswordDto, BindingResult result) {
        model.addAttribute(RESET_LINK_MODEL_ATTRIBUTE_NAME, resetPasswordDto.getResetLink());
        if (isResultOk(result, model)) {
            passResetDataToResetPasswordService(model, resetPasswordDto);
        }
        return RESET_PASSWORD_DONE_VIEW;
    }


    private boolean isResultOk(BindingResult result, Model model) {
        BindingResultErrorParser errorParser = new BindingResultErrorParser
                .Builder(result, model).messageName(FORM_ERROR_MESSAGE_ATTRIBUTE_NAME).build();
        return errorParser.isResultOk();
    }


    private void addDtoObjectsToModel(Model model) {
        addResetPasswordDtoToModel(model);
        addResetPasswordEmailDtoToModel(model);
    }


    private void addResetPasswordEmailDtoToModel(Model model) {
        ResetPasswordEmailDto resetPasswordEmailDto = new ResetPasswordEmailDto();
        model.addAttribute("resetPasswordEmailDto", resetPasswordEmailDto);
    }


    private void addResetPasswordDtoToModel(Model model) {
        ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
        model.addAttribute("resetPasswordDto", resetPasswordDto);
    }


    private void passResetDataToResetPasswordService(Model model, ResetPasswordDto resetPasswordDto) {
        try {
            resetPasswordService.resetPassword(resetPasswordDto);
            model.addAttribute(SUCCESS_MODEL_ATTRIBUTE_NAME, true);
        } catch (HttpServerErrorException | HttpClientErrorException exception) {
            model.addAttribute(SUCCESS_MODEL_ATTRIBUTE_NAME, false);
            model.addAttribute(FORM_ERROR_MESSAGE_ATTRIBUTE_NAME, exception.getResponseBodyAsString());
        }
    }


    private void passEmailDataToResetPasswordService(ResetPasswordEmailDto resetPasswordEmailDto, Model model) {
        try {
            resetPasswordService.sendConfirmationEmail(resetPasswordEmailDto.getEmail());
            model.addAttribute(SUCCESS_MODEL_ATTRIBUTE_NAME, true);
            model.addAttribute(SENDING_STATUS_MODEL_ATTRIBUTE_NAME, "The email was sent successfully.");
        } catch (HttpServerErrorException | HttpClientErrorException exception) {
            model.addAttribute(SENDING_STATUS_MODEL_ATTRIBUTE_NAME, exception.getResponseBodyAsString());
            model.addAttribute(SUCCESS_MODEL_ATTRIBUTE_NAME, false);
        }
    }
}
