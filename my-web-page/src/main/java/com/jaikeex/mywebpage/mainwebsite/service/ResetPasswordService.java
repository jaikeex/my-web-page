package com.jaikeex.mywebpage.mainwebsite.service;

import com.jaikeex.mywebpage.config.connection.ServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.connection.MwpServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.UserServiceDownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResetPasswordService {

    private static final Class<? extends ServiceDownException> SERVICE_EXCEPTION_TYPE = UserServiceDownException.class;

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    private final ServiceRequest serviceRequest;

    @Autowired
    public ResetPasswordService(MwpServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
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
        sendPostRequestToUserService(resetPasswordDto);
    }

    private void sendPostRequestToUserService(ResetPasswordDto resetPasswordDto) {
        String url = getResetPasswordUrl(resetPasswordDto);
        serviceRequest.sendPatchRequest(url, resetPasswordDto.getPassword(), SERVICE_EXCEPTION_TYPE);
    }

    private String getResetPasswordUrl(ResetPasswordDto resetPasswordDto) {
        return apiGatewayUrl + "users/password/email/" + resetPasswordDto.getEmail() + "/token/" + resetPasswordDto.getToken();
    }

    public void sendConfirmationEmail(String email) {
        String url = apiGatewayUrl + "users/reset-password/email/" + email;
        serviceRequest.sendGetRequest(url, Object.class, SERVICE_EXCEPTION_TYPE);
    }
}


