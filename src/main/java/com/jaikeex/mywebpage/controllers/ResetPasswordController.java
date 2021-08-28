package com.jaikeex.mywebpage.controllers;


import com.jaikeex.mywebpage.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.services.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ResetPasswordController {

    ResetPasswordService resetPasswordService;

    @Autowired
    public ResetPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @GetMapping("/user/reset-password")
    public String resetPassword(Model model) {
        ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
        model.addAttribute("resetPasswordDto", resetPasswordDto);
        return "/user/reset-password";
    }

    @PostMapping("/user/reset-password")
    public String resetPasswordForm(@Valid ResetPasswordDto resetPasswordDto, BindingResult result) {
        resetPasswordService.sendConfirmationEmail(resetPasswordDto.getEmail());
        return "/user/reset-password";
    }

    @PostMapping("/user/reset-password-done")
    public String resetPasswordConfirm(Model model, @Valid ResetPasswordDto resetPasswordDto, BindingResult result) {
        model.addAttribute("resetLink", resetPasswordDto.getResetLink());
        if (result.hasErrors()) {
            parseSignupErrors(result, model);
            return "/user/reset-password-done";
        }
        if (resetPasswordService.resetPassword(resetPasswordDto)) {
            model.addAttribute("success", true);
            return "/user/reset-password-done";
        } else {
            model.addAttribute("errorMessage", "There was an error resetting your password");
        }
        return "/user/reset-password-done";
    }

    private void parseSignupErrors (BindingResult result, Model model) {
        for (ObjectError error : result.getAllErrors()) {
            if (error.toString().contains("Blank")) {
                model.addAttribute("errorMessage", "Please fill in all the fields!");
            }
            if (error.toString().contains("Pattern.email")) {
                model.addAttribute("errorMessage", "Wrong email!");
            }
            if (error.toString().contains("PasswordMatches")) {
                model.addAttribute("errorMessage", "The passwords didn't match!");
            }
        }
    }


}
