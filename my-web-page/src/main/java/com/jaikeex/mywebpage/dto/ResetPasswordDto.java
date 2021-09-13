package com.jaikeex.mywebpage.dto;

import com.jaikeex.mywebpage.utility.security.MyPasswordEncoder;
import com.jaikeex.mywebpage.utility.validators.MatchingPasswords;
import com.jaikeex.mywebpage.utility.validators.PasswordMatches;
import com.jaikeex.mywebpage.utility.validators.ValidateEmail;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;

@Data
@PasswordMatches.List({@PasswordMatches(field = "password", fieldMatch = "passwordForValidation")})
public class ResetPasswordDto implements MatchingPasswords {

    private PasswordEncoder encoder = new MyPasswordEncoder();


    @ValidateEmail
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordForValidation;
    private String resetLink;
    private String token;

    public ResetPasswordDto() {
    }

    public ResetPasswordDto(String email, String token, String password, String passwordForValidation) {
        this.email = email;
        this.token = token;
        this.password = password;
        this.passwordForValidation = passwordForValidation;
    }

    public String getResetLink() {
        return String.format("http://localhost:8080/user/reset-password?token=%s&email=%s", token, email);
    }

    public void encodePassword() {
        this.password = encoder.encode(password);
    }
}
