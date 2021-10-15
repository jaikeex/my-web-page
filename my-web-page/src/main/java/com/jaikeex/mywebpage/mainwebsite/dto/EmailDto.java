package com.jaikeex.mywebpage.mainwebsite.dto;

import com.jaikeex.mywebpage.mainwebsite.utility.validators.ValidateEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {

    @ValidateEmail
    private String sender;
    @NotBlank
    private String subject;
    @NotBlank
    private String messageText;
}
