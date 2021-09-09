package com.jaikeex.userservice.service.exception;

public class UserAlreadyExistsException extends Exception{

    public UserAlreadyExistsException() {
        super("User with these credentials already exists");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
