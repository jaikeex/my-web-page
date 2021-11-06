package com.jaikeex.mywebpage.issuetracker.utility.exception;

import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;

public class IssueServiceDownException extends ServiceDownException {
    public IssueServiceDownException() {
        super("Requested service is unreachable at the moment.");
    }

    public IssueServiceDownException(String message) {
        super(message);
    }
}
