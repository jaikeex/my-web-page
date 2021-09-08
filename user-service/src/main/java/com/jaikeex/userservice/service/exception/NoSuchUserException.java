package com.jaikeex.userservice.service.exception;

public class NoSuchUserException extends Exception {
    public NoSuchUserException () {
        super("No such user exists");
    }

    public NoSuchUserException(String message) {
        super(message);
    }
}
