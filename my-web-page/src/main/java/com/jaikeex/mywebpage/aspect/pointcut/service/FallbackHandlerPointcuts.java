package com.jaikeex.mywebpage.aspect.pointcut.service;

import com.jaikeex.mywebpage.config.connection.FallbackHandler;
import org.aspectj.lang.annotation.Pointcut;

public class FallbackHandlerPointcuts {

    @Pointcut("execution(* throwBackendServiceException(..)) && target(fallbackHandler)")
    public void fallbackHandlerPointcut(FallbackHandler fallbackHandler) {
    }
}