package com.jaikeex.mywebpage.jpa.dto;

import com.jaikeex.mywebpage.services.security.PasswordMatches;

@PasswordMatches.List({@PasswordMatches(field = "password", fieldMatch = "passwordForValidation")})
public class UserDto {

    private String username;
    private String password;
    private String passwordForValidation;

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
        System.out.println(username);
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        System.out.println(password);
        this.password = password;
    }

    public String getPasswordForValidation() {
        return passwordForValidation;
    }

    public void setPasswordForValidation(String passwordForValidation) {
        System.out.println(passwordForValidation);
        this.passwordForValidation = passwordForValidation;
    }
}
