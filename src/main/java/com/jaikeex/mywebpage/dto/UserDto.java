package com.jaikeex.mywebpage.dto;

import com.jaikeex.mywebpage.services.security.PasswordMatches;

@PasswordMatches.List({@PasswordMatches(field = "password", fieldMatch = "passwordForValidation")})
public class UserDto {

    protected String username;
    protected String password;
    protected String passwordForValidation;

    public UserDto() {
    }

    public UserDto(String username, String password, String passwordForValidation) {
        this.username = username;
        this.password = password;
        this.passwordForValidation = passwordForValidation;
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
