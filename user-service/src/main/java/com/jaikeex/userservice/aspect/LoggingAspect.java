package com.jaikeex.userservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {}

    @Pointcut("within(com.jaikeex.userservice.service..*)" +
            " || within(com.jaikeex.userservice.controller..*)")
    public void userServiceApplicationPackagePointcut() {}


    @Around("userServiceApplicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logWhenEnteringMethodBody(joinPoint);
        try {
            Object result = joinPoint.proceed();
            logWhenExitingMethodBody(joinPoint);
            return result;
        } catch (IllegalArgumentException exception) {
            logIllegalArgumentException(joinPoint);
            throw exception;
        }
    }

    private void logIllegalArgumentException(JoinPoint joinPoint) {
        log.error("Illegal argument: {} in {}.{}()",
                Arrays.toString(joinPoint.getArgs()),
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }

    private void logWhenEnteringMethodBody(JoinPoint joinPoint) {
        log.debug("Enter [method={}.{}({})]",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                getArgsAsString(joinPoint));
    }

    private void logWhenExitingMethodBody(JoinPoint joinPoint) {
        log.debug("Exit [method={}.{}({})]",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                getArgsAsString(joinPoint));
    }

    private String getArgsAsString(JoinPoint joinPoint) {
        return Arrays.toString(joinPoint.getArgs());
    }
}