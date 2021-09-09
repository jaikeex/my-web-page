package com.jaikeex.emailservice.controller;

import com.jaikeex.emailservice.dto.EmailDto;
import com.jaikeex.emailservice.entity.Email;
import com.jaikeex.emailservice.service.MyEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return getEmailNotSendResponseEntity();
        }
    }

    private ResponseEntity<Object> getOkEmailResponseEntity(Email email) {
        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    private ResponseEntity<Object> getEmailNotSendResponseEntity() {
        return new ResponseEntity<>("There was an error sending the email", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
