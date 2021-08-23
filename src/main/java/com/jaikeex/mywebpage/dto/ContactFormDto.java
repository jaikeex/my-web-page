package com.jaikeex.mywebpage.dto;

import com.jaikeex.mywebpage.services.validators.ValidateEmail;

public class ContactFormDto {

    @ValidateEmail
    private String email;
    private String subject;
    private String messageText;

    public ContactFormDto() {
    }

    public ContactFormDto(String email, String subject, String messageText) {
        this.email = email;
        this.subject = subject;
        this.messageText = messageText;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
