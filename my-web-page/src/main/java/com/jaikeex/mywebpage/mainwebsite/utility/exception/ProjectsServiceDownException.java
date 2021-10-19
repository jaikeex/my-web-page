package com.jaikeex.mywebpage.mainwebsite.utility.exception;

public class ProjectsServiceDownException extends ServiceDownException{
    public ProjectsServiceDownException() {
        super("Projects service is unreachable at the moment.");
    }

    public ProjectsServiceDownException(String message) {
        super(message);
    }
}
