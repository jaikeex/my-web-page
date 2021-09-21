package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.model.User;
import com.jaikeex.mywebpage.restemplate.RestTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

@Service
public class ResetPasswordService {

    RestTemplateFactory restTemplateFactory;

    @Autowired
    public ResetPasswordService(RestTemplateFactory restTemplateFactory) {
        this.restTemplateFactory = restTemplateFactory;
    }

    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        resetPasswordDto.encodePassword();
        restTemplate.patchForObject(
                    API_GATEWAY_URL + "users/password/email/" + resetPasswordDto.getEmail() + "/token/" + resetPasswordDto.getToken(),
                    resetPasswordDto.getPassword(),
                    User.class);
    }

    public void sendConfirmationEmail(String email) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
            restTemplate.getForEntity(API_GATEWAY_URL + "users/reset-password/email/" + email, User.class);
    }
}


