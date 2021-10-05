package com.jaikeex.userservice.service.exception;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException () {
        super("No such user exists");
    }

    public NoSuchUserException(String message) {
        super(message);
    }
}
