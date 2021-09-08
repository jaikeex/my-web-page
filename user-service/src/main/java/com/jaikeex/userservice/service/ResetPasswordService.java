package com.jaikeex.userservice.service;

import com.jaikeex.userservice.dto.Email;
import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.repository.UserRepository;
import com.jaikeex.userservice.service.exception.EmailServiceDownException;
import com.jaikeex.userservice.service.exception.NoSuchUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Random;

import static com.jaikeex.userservice.UserServiceApplication.API_GATEWAY_URL;

@Service
@Slf4j
public class ResetPasswordService {

    UserRepository repository;
    PasswordEncoder encoder;
    RestTemplate restTemplate;


    private final String subject = "Password reset requested";

    private final String emailBody =
            "Hello!\nYou requested to reset your password for www.kubahruby.com.\n " +
                    "Please follow this link:\n";

    private final String emailFooter =
            "\nIf you have not requested this email, please ignore it.\n" +
                    "Best wishes, Jakub Hrubý.";


    @Autowired
    public ResetPasswordService(UserRepository repository, MyPasswordEncoder encoder, RestTemplate restTemplate) {
        this.repository = repository;
        this.encoder = encoder;
        this.restTemplate = restTemplate;

    }

    public void sendResetPasswordConfirmationEmail(String email) throws Exception {
        //RestTemplate restTemplate = new RestTemplate();

        User user = repository.findByEmail(email);
        if (user == null) {
            throw new NoSuchUserException("No user with this email exists");
        }
        user.setResetPasswordToken(generateResetPasswordToken());
        Email emailObject = getEmailObjectWithResetPasswordData(user);

        try {
            log.info("Sending reset password confirmation email to " + email);
            restTemplate.postForEntity(API_GATEWAY_URL + "/emails/", emailObject, Email.class);
        } catch (HttpServerErrorException exception) {
            log.info("Sending reset password confirmation email failed");
            throw new EmailServiceDownException(exception.getResponseBodyAsString());
        }
    }

    private Email getEmailObjectWithResetPasswordData(User user) {
        Email email = new Email();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        email.setRecipient(user.getEmail());
        email.setSubject(subject);
        email.setMessage(constructResetPasswordMessage(user));
        email.setDate(now);
        return email;
    }

    private String constructResetLink(User user) {
        String resetToken = user.getResetPasswordToken();
        user.setResetPasswordToken(encoder.encode(resetToken));
        repository.save(user);
        String email = user.getEmail();

        return String.format("http://localhost:8080/user/reset-password?token=%s&email=%s", resetToken, email);
    }

    private String constructResetPasswordMessage(User user) {
        String resetLink = constructResetLink(user);
        return (emailBody + resetLink + emailFooter);
    }

    private String generateTokenForUser() {
        return encoder.encode(generateResetPasswordToken());
    }


    private String generateResetPasswordToken() {
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


}
