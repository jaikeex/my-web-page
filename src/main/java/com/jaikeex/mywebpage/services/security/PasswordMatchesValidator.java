package com.jaikeex.mywebpage.services.security;

import com.jaikeex.mywebpage.dto.UserDto;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserDto> {
    private String field;
    private String fieldMatch;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext constraintValidatorContext) {

        Object fieldValue = new BeanWrapperImpl(userDto)
                .getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(userDto)
                .getPropertyValue(fieldMatch);

        boolean isValid;

        if (fieldValue != null) {
            isValid = fieldValue.equals(fieldMatchValue);
        } else {
            return fieldMatchValue == null;
        }

        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "The form was incorrect").addConstraintViolation();
        }
        return isValid;
    }
}
