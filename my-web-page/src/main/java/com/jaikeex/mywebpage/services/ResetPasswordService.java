package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.model.User;
import com.jaikeex.mywebpage.restemplate.RestTemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

@Service
@Slf4j
public class ResetPasswordService {

    RestTemplateFactory restTemplateFactory;

    @Autowired
    public ResetPasswordService(RestTemplateFactory restTemplateFactory) {
        this.restTemplateFactory = restTemplateFactory;
    }

    /**Sends the request to the user service to update password in database based
     * on the data provided in argument dto.
     * @param resetPasswordDto Data transfer object with necessary data for
     *                         the user service to complete the reset password
     *                         process.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws org.springframework.web.client.HttpServerErrorException
     *          Whenever a 5xx http status code gets returned.
     */
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        resetPasswordDto.encodePassword();
        sendResetPasswordRequestToUserService(resetPasswordDto);
    }

    private void sendResetPasswordRequestToUserService(ResetPasswordDto resetPasswordDto) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        restTemplate.patchForObject(
                    API_GATEWAY_URL + "users/password/email/" + resetPasswordDto.getEmail() + "/token/" + resetPasswordDto.getToken(),
                    resetPasswordDto.getPassword(),
                    User.class);
        log.info("Sent a request to the user service to change password in database [email={}]", resetPasswordDto.getEmail());
    }

    public void sendConfirmationEmail(String email) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        restTemplate.getForEntity(API_GATEWAY_URL + "users/reset-password/email/" + email, User.class);
        log.info("Sent a request to the user service to process the reset password confirmation email [email={}]", email);
    }
}


