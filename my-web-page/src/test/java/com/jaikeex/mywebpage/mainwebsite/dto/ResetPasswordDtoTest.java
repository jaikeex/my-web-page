package com.jaikeex.mywebpage.mainwebsite.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


class ResetPasswordDtoTest {

    private final ResetPasswordDto resetPasswordDto = new ResetPasswordDto(
            "",
            "",
            "testuserfordbaccess",
            "testuserfordbaccess");

    @Test
    void setEmail() {
        resetPasswordDto.setEmail("email");
        assertTrue(resetPasswordDto.getResetLink().contains("email=email"));
    }

    @Test
    void setToken() {
        resetPasswordDto.setToken("token");
        assertTrue(resetPasswordDto.getResetLink().contains("/user/reset-password?token=token"));

    }
}