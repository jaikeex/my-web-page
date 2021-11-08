package com.jaikeex.mywebpage.mainwebsite.utility.validators;

import com.jaikeex.mywebpage.mainwebsite.dto.UserRegistrationFormDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestComponent
@ExtendWith(SpringExtension.class)
class PasswordMatchesValidatorTest {

    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private final UserRegistrationFormDto userDto = new UserRegistrationFormDto(
            "testuserfordbaccess@testuserfordbaccess.com",
            "testuserfordbaccess",
            "testuserfordbaccess",
            "testuserfordbaccess");


    @Test
    public void userDtoValidFormNoViolations() {
        Set<ConstraintViolation<UserRegistrationFormDto>> violations = validator.validate(userDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void userDtoNoMatchingPasswords() {
        userDto.setPassword("test");
        userDto.setPasswordForValidation("fail");
        Set<ConstraintViolation<UserRegistrationFormDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void userDtoBlankUsername() {
        userDto.setUsername("");
        Set<ConstraintViolation<UserRegistrationFormDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void userDtoBlankPassword() {
        userDto.setPassword("");
        Set<ConstraintViolation<UserRegistrationFormDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void userDtoBlankPasswordForValidation() {
        userDto.setPasswordForValidation("");
        Set<ConstraintViolation<UserRegistrationFormDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void userDtoInvalidEmailNoAt() {
        userDto.setEmail("test.com");
        Set<ConstraintViolation<UserRegistrationFormDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void userDtoInvalidEmailNoDomain() {
        userDto.setEmail("test@gmail");
        Set<ConstraintViolation<UserRegistrationFormDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void userDtoInvalidEmailWrongLetters() {
        userDto.setEmail("ěščřžýáí@gmail.com");
        Set<ConstraintViolation<UserRegistrationFormDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

}