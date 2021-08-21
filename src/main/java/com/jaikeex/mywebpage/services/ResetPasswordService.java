package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.entity.User;
import com.jaikeex.mywebpage.jpa.UserRepository;
import com.jaikeex.mywebpage.services.security.MyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ResetPasswordService extends MyEmailService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    private final String emailBody =
                    "You requested to reset your password for www.kubahruby.com.\n " +
                    "Please follow this link:\n";

    @Autowired
    public ResetPasswordService(UserRepository repository, MyPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public void sendConfirmationEmail (String email) {
        User user = repository.findByEmail(email);
        String resetToken = generateToken();
        String subject = "Password reset requested";
        if (user != null) {
            user.setResetPasswordToken(encoder.encode(resetToken));
            repository.save(user);
            this.setTo(email);
            sendMessage(subject, constructEmail(constructResetLink(resetToken), email));
        }
    }

    private String constructResetLink(String token) {
        return String.format("http://localhost:8080/user/reset-password?token=%s", token);
    }

    private String constructEmail(String resetLink, String email) {
        return emailBody + resetLink + String.format("&email=%s", email);
    }

    private String generateToken() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 32;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public boolean resetPassword(ResetPasswordDto resetPasswordDto) {
        User user = repository.findByEmail(resetPasswordDto.getEmail());
        if (tokenMatches(user.getResetPasswordToken(), resetPasswordDto.getToken())) {
            user.setPassword(resetPasswordDto.getPassword());
            repository.save(user);
            return true;
        }
        return false;
    }

    private boolean tokenMatches(String userToken, String token) {
        return encoder.matches(token, userToken);
    }



}
