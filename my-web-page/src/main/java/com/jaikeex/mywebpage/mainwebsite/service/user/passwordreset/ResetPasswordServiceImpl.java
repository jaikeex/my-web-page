package com.jaikeex.mywebpage.mainwebsite.service.user.passwordreset;

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
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private static final Class<? extends ServiceDownException> SERVICE_EXCEPTION_TYPE = UserServiceDownException.class;

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    private final ServiceRequest serviceRequest;

    @Autowired
    public ResetPasswordServiceImpl(MwpServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        resetPasswordDto.encodePassword();
        sendPostRequestToUserService(resetPasswordDto);
    }

    @Override
    public void sendConfirmationEmail(String email) {
        String url = apiGatewayUrl + "users/reset-password/email/" + email;
        serviceRequest.sendGetRequest(url, Object.class, SERVICE_EXCEPTION_TYPE);
    }

    private void sendPostRequestToUserService(ResetPasswordDto resetPasswordDto) {
        String url = getResetPasswordUrl(resetPasswordDto);
        serviceRequest.sendPatchRequest(url, resetPasswordDto.getPassword(), SERVICE_EXCEPTION_TYPE);
    }

    private String getResetPasswordUrl(ResetPasswordDto resetPasswordDto) {
        return apiGatewayUrl + "users/password/email/" + resetPasswordDto.getEmail() + "/token/" + resetPasswordDto.getToken();
    }

}


