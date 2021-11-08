package com.jaikeex.mywebpage.aspect.pointcut.service;

import com.jaikeex.mywebpage.config.connection.ServiceRequest;
import org.aspectj.lang.annotation.Pointcut;

public class ServiceRequestPointcuts {

    @Pointcut("execution(* sendGetRequest(..)) && target(serviceRequest)")
    public void getRequestPointcut(ServiceRequest serviceRequest) {
    }

    @Pointcut("execution(* sendPostRequest(..)) && target(serviceRequest)")
    public void postRequestPointcut(ServiceRequest serviceRequest) {
    }

    @Pointcut("execution(* updateIssueWithNewProperties(..)) && target(serviceRequest)")
    public void patchRequestPointcut(ServiceRequest serviceRequest) {
    }
}