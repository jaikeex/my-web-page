package com.jaikeex.mywebpage.mainwebsite.utility.exception;

public class ServiceDownException extends RuntimeException {
    public ServiceDownException() {
        super("Requested service is not responding.");
    }
    public ServiceDownException(String message) {
        super(message);
    }
}
