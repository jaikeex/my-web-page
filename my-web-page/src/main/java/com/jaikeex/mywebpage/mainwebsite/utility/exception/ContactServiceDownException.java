package com.jaikeex.mywebpage.mainwebsite.utility.exception;

public class ContactServiceDownException extends ServiceDownException {
    public ContactServiceDownException() {
        super("Contact service is unreachable at the moment.");
    }

    public ContactServiceDownException(String message) {
        super(message);
    }
}
