package com.jaikeex.mywebpage.mainwebsite.service;

import com.jaikeex.mywebpage.config.circuitbreaker.FallbackHandler;
import com.jaikeex.mywebpage.config.circuitbreaker.qualifier.CircuitBreakerName;
import com.jaikeex.mywebpage.mainwebsite.controller.fallback.MwpFallbackHandler;
import com.jaikeex.mywebpage.mainwebsite.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.mainwebsite.model.User;
import com.jaikeex.mywebpage.resttemplate.RestTemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ResetPasswordService {

    private static final String RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER = "RESET_PASSWORD_SERVICE_CB";

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    private final RestTemplate restTemplate;
    private final UserService userService;
    private final FallbackHandler fallbackHandler;
    private final CircuitBreaker circuitBreaker;

    @Autowired
    public ResetPasswordService(RestTemplateFactory restTemplateFactory,
                                UserService userService,
                                MwpFallbackHandler fallbackHandler,
                                @CircuitBreakerName(RESET_PASSWORD_SERVICE_CIRCUIT_BREAKER) CircuitBreaker circuitBreaker) {
        this.restTemplate = restTemplateFactory.getRestTemplate();
        this.userService = userService;
        this.fallbackHandler = fallbackHandler;
        this.circuitBreaker = circuitBreaker;
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
        String url = getResetPasswordUrl(resetPasswordDto);
        resetPasswordDto.encodePassword();
        sendPostRequestToUserService(resetPasswordDto);
    }

    private void sendPostRequestToUserService(ResetPasswordDto resetPasswordDto) {
        String url = getResetPasswordUrl(resetPasswordDto);
        restTemplate.patchForObject(url, resetPasswordDto.getPassword(), User.class);
    }

    private String getResetPasswordUrl(ResetPasswordDto resetPasswordDto) {
        return apiGatewayUrl + "users/password/email/" + resetPasswordDto.getEmail() + "/token/" + resetPasswordDto.getToken();
    }

    public void sendConfirmationEmail(String email) {
        String url = apiGatewayUrl + "users/reset-password/email/" + email;
        restTemplate.getForEntity(url, User.class);
    }
}


