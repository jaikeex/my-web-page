package com.jaikeex.userservice.service;

import com.jaikeex.userservice.dto.Email;
import com.jaikeex.userservice.entity.User;
import com.jaikeex.userservice.repository.UserRepository;
import com.jaikeex.userservice.restemplate.RestTemplateFactory;
import com.jaikeex.userservice.service.exception.EmailServiceDownException;
import com.jaikeex.userservice.service.exception.NoSuchUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Random;

@Service
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ResetPasswordEmailService {

    UserRepository repository;
    PasswordEncoder encoder;
    RestTemplateFactory restTemplateFactory;

    String email;
    String resetToken;

    @Autowired
    public ResetPasswordEmailService(UserRepository repository,
                                     MyPasswordEncoder encoder,
                                     RestTemplateFactory restTemplateFactory) {
        this.repository = repository;
        this.encoder = encoder;
        this.restTemplateFactory = restTemplateFactory;
    }


    public void sendResetPasswordConfirmationEmail(String emailAddressArgument) throws Exception {
        log.debug("entering sendResetPasswordConfirmationEmail");
        this.email = emailAddressArgument;
        this.resetToken = generateResetPasswordToken();
        saveUserWithEncodedTokenToDatabase(email);
        Email emailObject = getEmailObjectWithResetPasswordData();
        try {
            postEmailObjectToEmailService(emailObject);
        } catch (HttpServerErrorException exception) {
            log.error("Sending reset password confirmation email failed", exception);
            throw new EmailServiceDownException(exception.getResponseBodyAsString());
        }
        log.debug("exiting sendResetPasswordConfirmationEmail");
    }


    private Email getEmailObjectWithResetPasswordData() {
        log.debug("Loading data into Email object");
        Email emailObject = new Email();
        String subject = "Password reset requested";
        Timestamp now = new Timestamp(System.currentTimeMillis());
        emailObject.setRecipient(email);
        emailObject.setSubject(subject);
        emailObject.setMessage(constructResetPasswordMessage());
        emailObject.setDate(now);
        return emailObject;
    }


    private String constructResetPasswordMessage() {
        log.debug("Constructing reset password message");
        String resetLink = constructResetLink();
        String emailFooter = "\nIf you have not requested " +
                "this email, please ignore it.\n" +
                "Best wishes, Jakub Hrubý.";
        String emailBody = "Hello!\nYou requested to reset " +
                "your password for www.kubahruby.com.\n " +
                "Please follow this link:\n";
        return (emailBody + resetLink + emailFooter);
    }


    private String constructResetLink() {
        log.debug("Creating reset password link");
        return String.format(
                "https://www.kubahruby.com/user/reset-password?token=%s&email=%s",
                resetToken, email);
    }


    private void saveUserWithEncodedTokenToDatabase(String email) throws NoSuchUserException {
        User user = repository.findUserByEmail(email);
        if (user == null) {
            log.warn("Email provided does not exist in database");
            throw new NoSuchUserException("No user with this email exists");
        }
        user.setResetPasswordToken(encoder.encode(resetToken));
        repository.save(user);
    }


    private void postEmailObjectToEmailService(Email emailObject) throws HttpServerErrorException{
        log.info("Sending reset password confirmation email to {}", email);
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        restTemplate.postForEntity(
                "http://email-service:8004/emails/",
                emailObject,
                Email.class);
    }


    private String generateResetPasswordToken() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 32;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }
}
