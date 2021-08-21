package com.jaikeex.mywebpage.services.validators;


import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ConstraintComposition(CompositionType.OR)
@Pattern.List({
        @Pattern(regexp=Constants.PATTERN, flags = Pattern.Flag.CASE_INSENSITIVE),
        @Pattern(regexp=Constants.EMPTY, flags = Pattern.Flag.CASE_INSENSITIVE)})
@Documented
public @interface ValidateEmail {
    String message() default "Wrong email";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}

interface Constants {
    static final String EMPTY = "";
    static final String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
    static final String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)+";
    static final String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

    static final String PATTERN =
            "^" + ATOM + "+(\\." + ATOM + "+)*@"
                    + DOMAIN
                    + "|"
                    + IP_DOMAIN
                    + ")$";
}

