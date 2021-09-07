package com.jaikeex.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String email;
    private String username;
    private String password;
    private String passwordForValidation;

}

