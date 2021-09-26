package com.jaikeex.mywebpage.dto;

import com.jaikeex.mywebpage.utility.validators.ValidateEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactFormDto {

    @ValidateEmail
    private String email;
    @NotBlank
    private String subject;
    @NotBlank
    private String messageText;
}
