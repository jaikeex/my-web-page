package com.jaikeex.mywebpage.mainwebsite.controller;


import com.jaikeex.mywebpage.mainwebsite.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.mainwebsite.dto.ResetPasswordFormDto;
import com.jaikeex.mywebpage.mainwebsite.service.user.passwordreset.ResetPasswordService;
import com.jaikeex.mywebpage.mainwebsite.utility.BindingResultErrorParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.Valid;

@Controller
@Slf4j
public class ResetPasswordController {

    //TODO: Implement centralized exception handling.

    private static final String FORM_ERROR_MESSAGE_ATTRIBUTE_NAME = "formErrorMessage";
    private static final String SUCCESS_ATTRIBUTE_NAME = "success";
    private static final String SENDING_STATUS_ATTRIBUTE_NAME = "message";
    private static final String RESET_LINK_ATTRIBUTE_NAME = "resetLink";
    private static final String RESET_PASSWORD_VIEW = "user/reset-password";
    private static final String RESET_PASSWORD_DONE_VIEW = "user/reset-password-done";
    private static final String RESET_PASSWORD_EMAIL_DTO_ATTRIBUTE_NAME = "resetPasswordEmailDto";
    private static final String RESET_PASSWORD_DTO_ATTRIBUTE_NAME = "resetPasswordDto";
    private static final String EMAIL_SENT_SUCCESSFULLY = "The email was sent successfully.";

    ResetPasswordService resetPasswordService;

    @Autowired
    public ResetPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @GetMapping("/user/reset-password")
    public String getResetPasswordPage(Model model) {
        addDtoObjectsToModel(model);
        return RESET_PASSWORD_VIEW;
    }

    @PostMapping("/user/reset-password")
    public String postResetPasswordForm(@Valid ResetPasswordFormDto resetPasswordEmailDto, BindingResult result, Model model) {
        if (isResultOk(result, model)) {
            passEmailDataToResetPasswordService(resetPasswordEmailDto, model);
        }
        return RESET_PASSWORD_VIEW;
    }

    @PostMapping("/user/reset-password-done")
    public String resetPasswordConfirmationPage(Model model, @Valid ResetPasswordDto resetPasswordDto, BindingResult result) {
        model.addAttribute(RESET_LINK_ATTRIBUTE_NAME, resetPasswordDto.getResetLink());
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
        ResetPasswordFormDto resetPasswordEmailDto = new ResetPasswordFormDto();
        model.addAttribute(RESET_PASSWORD_EMAIL_DTO_ATTRIBUTE_NAME, resetPasswordEmailDto);
        log.debug("Added attribute to model [name={}, value={}]", RESET_PASSWORD_EMAIL_DTO_ATTRIBUTE_NAME, resetPasswordEmailDto);
    }

    private void addResetPasswordDtoToModel(Model model) {
        ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
        model.addAttribute(RESET_PASSWORD_DTO_ATTRIBUTE_NAME, resetPasswordDto);
        log.debug("Added attribute to model [name={}, value={}]", RESET_PASSWORD_DTO_ATTRIBUTE_NAME, resetPasswordDto);
    }

    private void passResetDataToResetPasswordService(Model model, ResetPasswordDto resetPasswordDto) {
        try {
            resetPasswordService.resetPassword(resetPasswordDto);
            appendModelWithFormOkAttributes(model);
        } catch (HttpServerErrorException | HttpClientErrorException exception) {
            log.warn(exception.getResponseBodyAsString());
            appendModelWithFormErrorAttributes(model, exception);
        }
    }

    private void passEmailDataToResetPasswordService(ResetPasswordFormDto resetPasswordEmailDto, Model model) {
        try {
            resetPasswordService.sendConfirmationEmail(resetPasswordEmailDto.getEmail());
            appendModelWithMessageSuccessAttributes(model);
        } catch (HttpServerErrorException | HttpClientErrorException exception) {
            log.warn(exception.getResponseBodyAsString());
            appendModelWithMessageFailedAttributes(model, exception);
        }
    }

    private void appendModelWithFormOkAttributes(Model model) {
        model.addAttribute(SUCCESS_ATTRIBUTE_NAME, true);
        log.debug("Added attribute to model [name={}, value={}]", SUCCESS_ATTRIBUTE_NAME, true);
    }

    private void appendModelWithFormErrorAttributes(Model model, HttpStatusCodeException exception) {
        String errorMessage = exception.getResponseBodyAsString();
        model.addAttribute(SUCCESS_ATTRIBUTE_NAME, false);
        log.debug("Added attribute to model [name={}, value={}]", SUCCESS_ATTRIBUTE_NAME, false);
        model.addAttribute(FORM_ERROR_MESSAGE_ATTRIBUTE_NAME, errorMessage);
        log.debug("Added attribute to model [name={}, value={}]", FORM_ERROR_MESSAGE_ATTRIBUTE_NAME, errorMessage);
    }
    
    private void appendModelWithMessageFailedAttributes(Model model, HttpStatusCodeException exception) {
        String errorMessage = exception.getResponseBodyAsString();
        model.addAttribute(SENDING_STATUS_ATTRIBUTE_NAME, errorMessage);
        log.debug("Added attribute to model [name={}, value={}]", SENDING_STATUS_ATTRIBUTE_NAME, errorMessage);
        model.addAttribute(SUCCESS_ATTRIBUTE_NAME, false);
        log.debug("Added attribute to model [name={}, value={}]", SUCCESS_ATTRIBUTE_NAME, false);
    }

    private void appendModelWithMessageSuccessAttributes(Model model) {
        model.addAttribute(SUCCESS_ATTRIBUTE_NAME, true);
        log.debug("Added attribute to model [name={}, value={}]", SUCCESS_ATTRIBUTE_NAME, true);
        model.addAttribute(SENDING_STATUS_ATTRIBUTE_NAME, EMAIL_SENT_SUCCESSFULLY);
        log.debug("Added attribute to model [name={}, value={}]", SENDING_STATUS_ATTRIBUTE_NAME, EMAIL_SENT_SUCCESSFULLY);
    }
}
