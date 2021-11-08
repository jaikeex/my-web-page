package com.jaikeex.mywebpage.mainwebsite.controller.advice;

import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class ServiceDownExceptionHandler {

    @Value("${spring.error-page-view:error}")
    private String errorPageView;
    @Value("${spring.error-message-attribute-name:errorMessage}")
    private String errorMessageAttributeName;

    @ExceptionHandler(ServiceDownException.class)
    public ModelAndView handleHttpException(ServiceDownException exception) {
        log.warn("{}", ExceptionUtils.getStackTrace(exception));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(errorMessageAttributeName, exception.getMessage());
        modelAndView.setViewName(errorPageView);
        return modelAndView;
    }
}
