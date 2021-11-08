package com.jaikeex.mywebpage.mainwebsite.dto;

import com.jaikeex.mywebpage.mainwebsite.utility.validators.ValidateEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordFormDto {

    @ValidateEmail
    @NotBlank
    private String email;
}
