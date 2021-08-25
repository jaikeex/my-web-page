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
    private final String subject = "Password reset requested";


    private final String emailBody =
            "Hello!\nYou requested to reset your password for www.kubahruby.com.\n " +
                    "Please follow this link:\n";

    private final String emailFooter =
            "\nIf you have not requested this email, please ignore it.\n" +
                    "Best wishes, Jakub Hrubý.";


    @Autowired
    public ResetPasswordService(UserRepository repository, MyPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public boolean resetPassword(ResetPasswordDto resetPasswordDto) {
        User user = repository.findByEmail(resetPasswordDto.getEmail());
        if (user != null) {
            return updatePasswordInDatabase(user, resetPasswordDto);
        }
        return false;
    }

    public void sendConfirmationEmail (String email) {
        User user = repository.findByEmail(email);
        if (user != null) {
            this.setTo(email);
            String resetToken = generateTokenForUser(user);
            String messageBody = constructEmail(constructResetLink(resetToken), email);
            sendMessage(subject, messageBody);
        }
    }

    private boolean updatePasswordInDatabase(User user, ResetPasswordDto resetPasswordDto) {
        if (tokenMatches(user.getResetPasswordToken(), resetPasswordDto.getToken())) {
            repository.updatePassword(encoder.encode(resetPasswordDto.getPassword()), user.getUsername());
            return true;
        }
        return false;
    }

    private String constructResetLink(String token) {
        return String.format("http://localhost:8080/user/reset-password?token=%s", token);
    }

    private String constructEmail(String resetLink, String email) {
        return emailBody + resetLink + String.format("&email=%s", email) + emailFooter;
    }

    private String generateTokenForUser(User user) {
        String resetToken = generateToken();
        user.setResetPasswordToken(encoder.encode(resetToken));
        repository.save(user);
        return resetToken;
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

    private boolean tokenMatches(String userToken, String token) {
        return encoder.matches(token, userToken);
    }
}
