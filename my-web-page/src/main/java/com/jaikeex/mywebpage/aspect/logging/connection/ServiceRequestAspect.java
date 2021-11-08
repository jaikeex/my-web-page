package com.jaikeex.mywebpage.aspect.logging.connection;

import com.jaikeex.mywebpage.config.connection.ServiceRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;


@Slf4j
@EnableAspectJAutoProxy
public class ServiceRequestAspect {

    @Around(value = "com.jaikeex.mywebpage.aspect.pointcut.service.ServiceRequestPointcuts.getRequestPointcut(serviceRequest)", argNames = "joinPoint, serviceRequest")
    public Object logGetRequest(ProceedingJoinPoint joinPoint, ServiceRequest serviceRequest) throws Throwable {
        String url = (String) joinPoint.getArgs()[0];
        log.info("Sending HTTP GET request [url={}]", url);
        Object result = joinPoint.proceed();
        log.info("HTTP GET request returned successfully [url={}; body={}]", url, result);
        return result;
    }

    @Around(value = "com.jaikeex.mywebpage.aspect.pointcut.service.ServiceRequestPointcuts.postRequestPointcut(serviceRequest)", argNames = "joinPoint, serviceRequest")
    public Object logPostRequest(ProceedingJoinPoint joinPoint, ServiceRequest serviceRequest) throws Throwable {
        String url = (String) joinPoint.getArgs()[0];
        log.info("Sending HTTP POST request [url={}]", url);
        Object result = joinPoint.proceed();
        log.info("HTTP POST request returned successfully [url={}; body={}]", url, result);
        return result;
    }

    @Around(value = "com.jaikeex.mywebpage.aspect.pointcut.service.ServiceRequestPointcuts.patchRequestPointcut(serviceRequest)", argNames = "joinPoint, serviceRequest")
    public Object logPatchRequest(ProceedingJoinPoint joinPoint, ServiceRequest serviceRequest) throws Throwable {
        String url = (String) joinPoint.getArgs()[0];
        log.info("Sending HTTP PATCH request [url={}]", url);
        Object result = joinPoint.proceed();
        log.info("HTTP PATCH request returned successfully [url={}; body={}]", url, result);
        return result;
    }
}
