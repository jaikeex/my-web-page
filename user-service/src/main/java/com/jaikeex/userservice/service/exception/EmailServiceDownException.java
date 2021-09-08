package com.jaikeex.userservice.service.exception;

public class EmailServiceDownException extends Exception {
    public EmailServiceDownException() {
        super("Email service not responding");
    }

    public EmailServiceDownException(String message) {
        super(message);
    }
}
