package com.jaikeex.mywebpage.mainwebsite.utility.exception;

public class RegistrationProcessFailedException extends Exception{
    public RegistrationProcessFailedException() {
        super("Registration process failed");
    }

    public RegistrationProcessFailedException(String message) {
        super(message);
    }
}
