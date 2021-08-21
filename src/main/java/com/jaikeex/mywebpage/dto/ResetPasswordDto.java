package com.jaikeex.mywebpage.dto;

import com.jaikeex.mywebpage.services.validators.PasswordMatches;
import com.jaikeex.mywebpage.services.validators.ValidateEmail;

@PasswordMatches.List({@PasswordMatches(field = "password", fieldMatch = "passwordForValidation")})
public class ResetPasswordDto implements MatchingPasswords {

    @ValidateEmail
    private String email;
    private String token;
    private String password;
    private String passwordForValidation;
    private String resetLink;

    public ResetPasswordDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        updateResetLink();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        updateResetLink();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordForValidation() {
        return passwordForValidation;
    }

    public void setPasswordForValidation(String passwordForValidation) {
        this.passwordForValidation = passwordForValidation;
    }

    private void updateResetLink() {
        this.resetLink = String.format("/user/reset-password?token=%s&email=%s", getToken(), getEmail());
    }

    public String getResetLink() {
        return resetLink;
    }
}
