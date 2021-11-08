package com.jaikeex.mywebpage.mainwebsite.dto;

import com.jaikeex.mywebpage.config.security.encoder.MyPasswordEncoder;
import com.jaikeex.mywebpage.mainwebsite.utility.validators.MatchingPasswords;
import com.jaikeex.mywebpage.mainwebsite.utility.validators.PasswordMatches;
import com.jaikeex.mywebpage.mainwebsite.utility.validators.ValidateEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches.List({@PasswordMatches(field = "password", fieldMatch = "passwordForValidation")})
public class UserRegistrationFormDto implements MatchingPasswords {

    MyPasswordEncoder encoder = new MyPasswordEncoder();

    @ValidateEmail
    protected String email;
    @NotBlank
    protected String username;
    @NotBlank
    protected String password;
    @NotBlank
    protected String passwordForValidation;

    public UserRegistrationFormDto(String email, String username, String password, String passwordForValidation) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.passwordForValidation = passwordForValidation;
    }

    public void encodePassword() {
        this.password = encoder.encode(password);
    }

    public void setEmail(String email) {
        if (!email.equals("")) {
            this.email = email;
        }
    }
}
