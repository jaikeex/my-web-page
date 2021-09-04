package com.jaikeex.registerservice.dto;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

public class UserDto{

    protected String email;

    protected String username;

    protected String password;

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
        if (!email.equals("")) {
            this.email = email;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordForValidation() {
        return passwordForValidation;
    }

    public void setPasswordForValidation(String passwordForValidation) {
        this.passwordForValidation = passwordForValidation;
    }
}
