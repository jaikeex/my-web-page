package com.jaikeex.userservice.service;

import com.jaikeex.userservice.dto.Email;
import com.jaikeex.userservice.restemplate.RestTemplateFactory;
import com.jaikeex.userservice.service.exception.EmailServiceDownException;
import com.jaikeex.userservice.service.exception.NoSuchUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ResetPasswordEmailService {

    private static final String SUBJECT = "Password reset requested";
    private static final String EMAIL_FOOTER = "\nIf you have not requested " +
            "this email, please ignore it.\n" +
            "Best wishes, Jakub Hrubý.";
    private static final String EMAIL_BODY = "Hello!\nYou requested to reset " +
            "your password for www.kubahruby.com.\n " +
            "Please follow this link:\n";
    private static final String EMAIL_SERVICE_HTTP_ADDRESS = "http://email-service:8004/emails/";
    private static final String RESET_LINK_BASE = "https://www.kubahruby.com/user/reset-password";

    private final UserService userService;
    private final RestTemplateFactory restTemplateFactory;

    private String email;
    private String resetToken;

    @Autowired
    public ResetPasswordEmailService(UserService service,
                                     RestTemplateFactory restTemplateFactory) {
        this.userService = service;
        this.restTemplateFactory = restTemplateFactory;
    }


    public void sendResetPasswordConfirmationEmail(String emailAddress) throws Exception {
        log.debug("entering sendResetPasswordConfirmationEmail");
        setupProperties(emailAddress);
        Email emailObject = getEmailObjectWithResetPasswordData();
        saveUserWithEncodedTokenToDatabase();
        sendDataToEmailService(emailObject);
        log.debug("exiting sendResetPasswordConfirmationEmail");
    }

    private void setupProperties(String emailAddressArgument) {
        // Always call before creating emailObject.
        this.email = emailAddressArgument;
        this.resetToken = generateResetPasswordToken();
    }

    private void sendDataToEmailService(Email emailObject) {
        try {
            postHttpRequestToEmailService(emailObject);
        } catch (HttpServerErrorException exception) {
            log.error("Sending reset password confirmation email failed", exception);
            throw new EmailServiceDownException(exception.getResponseBodyAsString());
        }
    }


    private Email getEmailObjectWithResetPasswordData() {
        log.debug("Loading data into Email object");
        return new Email.Builder(email).subject(SUBJECT)
                .message(constructResetPasswordMessage()).build();
    }


    private String constructResetPasswordMessage() {
        log.debug("Constructing reset password message");
        String resetLink = constructResetLink();
        return (EMAIL_BODY + resetLink + EMAIL_FOOTER);
    }


    private String constructResetLink() {
        log.debug("Creating reset password link");
        return String.format(
                RESET_LINK_BASE + "?token=%s&email=%s", resetToken, email);
    }


    private void saveUserWithEncodedTokenToDatabase() throws NoSuchUserException {
        userService.saveUserWithEncodedResetTokenToDatabase(email, resetToken);
    }


    private void postHttpRequestToEmailService(Email emailObject) throws HttpServerErrorException{
        log.info("Sending reset password confirmation email to {}", email);
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        restTemplate.postForEntity(
                EMAIL_SERVICE_HTTP_ADDRESS,
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
