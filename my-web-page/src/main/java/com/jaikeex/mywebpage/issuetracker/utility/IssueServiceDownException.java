package com.jaikeex.mywebpage.issuetracker.utility;

public class IssueServiceDownException extends RuntimeException{
    public IssueServiceDownException() {
        super("Requested service is unreachable at the moment.");
    }

    public IssueServiceDownException(String message) {
        super(message);
    }
}
