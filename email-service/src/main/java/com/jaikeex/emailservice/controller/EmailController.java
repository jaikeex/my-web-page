package com.jaikeex.emailservice.controller;

import com.jaikeex.emailservice.entity.Email;
import com.jaikeex.emailservice.service.MyEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/emails")
public class EmailController {

    MyEmailService emailService;

    @Autowired
    public EmailController(MyEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> sendEmail(@RequestBody Email email) {
        try {
            emailService.sendMessage(email);
            return getOkEmailResponseEntity(email);
        } catch (MessagingException exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            return getEmailNotSendResponseEntity(exception.getMessage());
        }
    }

    private ResponseEntity<Object> getOkEmailResponseEntity(Email email) {
        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    private ResponseEntity<Object> getEmailNotSendResponseEntity(String errorMessage) {
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
