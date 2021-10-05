package com.jaikeex.userservice.service.exception;

public class InvalidResetTokenException extends RuntimeException{
    public InvalidResetTokenException() {
        super("Invalid reset password token");
    }

    public InvalidResetTokenException(String message) {
        super(message);
    }
}
