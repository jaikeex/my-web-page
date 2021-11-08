package com.jaikeex.mywebpage.mainwebsite.service.user.passwordreset;

import com.jaikeex.mywebpage.mainwebsite.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.UserServiceDownException;

/**
 * Manages the process of resetting users' passwords.
 */
public interface ResetPasswordService {

    /**Sends the request to the user service to update password in database based
     * on the data provided in argument dto.
     * @param resetPasswordDto Data transfer object with necessary data for
     *                         the user service to complete the reset password
     *                         process.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws UserServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    void resetPassword(ResetPasswordDto resetPasswordDto);

    /**Sends the request to the email service to send a reset password
     * confirmation email to the address provided by the user.
     * @param email Email object.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws UserServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    void sendConfirmationEmail(String email);
}
