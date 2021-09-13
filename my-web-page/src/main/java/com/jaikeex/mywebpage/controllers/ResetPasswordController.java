package com.jaikeex.mywebpage.controllers;


import com.jaikeex.mywebpage.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.dto.ResetPasswordEmailDto;
import com.jaikeex.mywebpage.services.ResetPasswordService;
import com.jaikeex.mywebpage.utility.BindingResultErrorParser;
import com.jaikeex.mywebpage.utility.exception.ResetPasswordProcessFailureException;
import com.jaikeex.mywebpage.utility.exception.SendingResetPasswordEmailFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ResetPasswordController {

    private static final String FORM_ERROR_MESSAGE_ATTRIBUTE_NAME = "formErrorMessage";


    ResetPasswordService resetPasswordService;

    @Autowired
    public ResetPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @GetMapping("/user/reset-password")
    public String getResetPasswordPage(Model model) {
        addDtoObjectsToModel(model);
        return "user/reset-password";
    }


    @PostMapping("/user/reset-password")
    public String postResetPasswordForm(@Valid ResetPasswordEmailDto resetPasswordEmailDto, BindingResult result, Model model) {
        if (isResultOk(result, model)) {
            try {
                resetPasswordService.sendConfirmationEmail(resetPasswordEmailDto.getEmail());
                model.addAttribute("success", true);
                model.addAttribute("message", "The email was sent successfully.");
            } catch (SendingResetPasswordEmailFailureException exception) {
                model.addAttribute("message", exception.getMessage());
                model.addAttribute("success", false);
            }
        }
        return "user/reset-password";
    }

    @PostMapping("/user/reset-password-done")
    public String resetPasswordConfirmationPage(Model model, @Valid ResetPasswordDto resetPasswordDto, BindingResult result) {
        model.addAttribute("resetLink", resetPasswordDto.getResetLink());
        if (isResultOk(result, model)) {
            try {
                resetPasswordService.resetPassword(resetPasswordDto);
                model.addAttribute("success", true);
            } catch (ResetPasswordProcessFailureException exception) {
                model.addAttribute("success", false);
                model.addAttribute("formErrorMessage", exception.getMessage());
            }
        }
        return "user/reset-password-done";
    }


    private boolean isResultOk(BindingResult result, Model model) {
        BindingResultErrorParser errorParser =
                new BindingResultErrorParser(result, model);
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

}
