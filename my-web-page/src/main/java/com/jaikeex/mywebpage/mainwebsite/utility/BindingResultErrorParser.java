package com.jaikeex.mywebpage.mainwebsite.utility;


import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BindingResultErrorParser {

    private static final String DEFAULT_FORM_ERROR_MESSAGE_ATTRIBUTE_NAME = "formErrorMessage";

    private final BindingResult result;
    private final Model model;
    private final String errorMessageAttributeName;


    public boolean isResultOk() {
        List<ObjectError> errors = result.getAllErrors();
        putResultErrorsIntoModel(errors, model);
        return errors.isEmpty();
    }

    public static class Builder {

        private final BindingResult result;
        private final Model model;

        private String messageName = DEFAULT_FORM_ERROR_MESSAGE_ATTRIBUTE_NAME;

        public Builder(BindingResult result, Model model) {
            this.result = result;
            this.model = model;
        }

        public Builder messageName(String messageName) {
            this.messageName = messageName;
            return this;
        }


        public BindingResultErrorParser build() {
            return new BindingResultErrorParser(this);
        }
    }


    private BindingResultErrorParser(Builder builder) {
        this.result = builder.result;
        this.model = builder.model;
        this.errorMessageAttributeName = builder.messageName;
    }


    private void putResultErrorsIntoModel(List<ObjectError> errors, Model model) {
        Map<String, Object> modelErrors = new HashMap<>();
        for (ObjectError error : errors) {
            if (error.toString().contains("Blank")) {
                modelErrors.put(errorMessageAttributeName, "Please fill in all the fields!");
            }
            if (error.toString().contains("Pattern.email")) {
                modelErrors.put(errorMessageAttributeName, "Wrong email!");
            }
            if (error.toString().contains("PasswordMatches")) {
                modelErrors.put(errorMessageAttributeName, "The passwords didn't match!");
            }
        }
        model.mergeAttributes(modelErrors);
    }


}
