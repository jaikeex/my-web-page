package com.jaikeex.mywebpage.mainwebsite.utility.exception;

public class UserServiceDownException extends ServiceDownException {
    public UserServiceDownException() {
        super("User service is unreachable at the moment.");
    }

    public UserServiceDownException(String message) {
        super(message);
    }
}
