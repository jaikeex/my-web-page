package com.jaikeex.mywebpage.aspect.logging.connection;

import com.jaikeex.mywebpage.config.connection.FallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class FallbackHandlerAspect {

    @Around(value = "com.jaikeex.mywebpage.aspect.pointcut.service.FallbackHandlerPointcuts.fallbackHandlerPointcut(fallbackHandler)", argNames = "joinPoint, fallbackHandler")
    public Object logGetRequest(ProceedingJoinPoint joinPoint, FallbackHandler fallbackHandler) throws Throwable {
        Throwable exception = (Throwable) joinPoint.getArgs()[1];
        log.warn("{}", ExceptionUtils.getStackTrace(exception));
        return joinPoint.proceed();
    }
}
