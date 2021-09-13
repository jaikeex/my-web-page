package com.jaikeex.mywebpage.dto;

import com.jaikeex.mywebpage.utility.validators.ValidateEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordEmailDto {

    @ValidateEmail
    @NotBlank
    private String email;

}
