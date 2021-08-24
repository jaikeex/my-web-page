package com.jaikeex.mywebpage.dto;

import com.jaikeex.mywebpage.services.validators.PasswordMatches;
import com.jaikeex.mywebpage.services.validators.ValidateEmail;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
@PasswordMatches.List({@PasswordMatches(field = "password", fieldMatch = "passwordForValidation")})
public class UserDto implements MatchingPasswords {


    @ValidateEmail
    protected String email;

    @NotBlank
    protected String username;

    @NotBlank
    protected String password;

    @NotBlank
    protected String passwordForValidation;

    public UserDto() {
    }

    public UserDto(String email, String username, String password, String passwordForValidation) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.passwordForValidation = passwordForValidation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
            this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
