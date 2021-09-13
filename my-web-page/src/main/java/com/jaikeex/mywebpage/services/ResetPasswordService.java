package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.model.User;
import com.jaikeex.mywebpage.utility.exception.ResetPasswordProcessFailureException;
import com.jaikeex.mywebpage.utility.exception.SendingResetPasswordEmailFailureException;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

@Service
public class ResetPasswordService {


    public void resetPassword(ResetPasswordDto resetPasswordDto) throws ResetPasswordProcessFailureException {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        resetPasswordDto.encodePassword();
        try {
            restTemplate.patchForObject(
                    API_GATEWAY_URL + "users/password/email/" + resetPasswordDto.getEmail() + "/token/" + resetPasswordDto.getToken(),
                    resetPasswordDto.getPassword(),
                    User.class);
        } catch (HttpServerErrorException | HttpClientErrorException exception) {
            throw new ResetPasswordProcessFailureException(exception.getResponseBodyAsString());
        }
    }

    public void sendConfirmationEmail(String email) throws SendingResetPasswordEmailFailureException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForEntity(API_GATEWAY_URL + "users/reset-password/email/" + email, User.class);
        } catch (HttpServerErrorException | HttpClientErrorException exception) {
            throw new SendingResetPasswordEmailFailureException(exception.getResponseBodyAsString());

        }
    }
}


