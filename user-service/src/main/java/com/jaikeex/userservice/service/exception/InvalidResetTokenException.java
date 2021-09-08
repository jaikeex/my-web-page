package com.jaikeex.userservice.service.exception;

public class InvalidResetTokenException extends Exception{
    public InvalidResetTokenException() {
        super("Invalid reset password token");
    }

    public InvalidResetTokenException(String message) {
        super(message);
    }
}
