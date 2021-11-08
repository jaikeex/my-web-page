package com.jaikeex.mywebpage.mainwebsite.service.user;

import com.jaikeex.mywebpage.mainwebsite.dto.UserRegistrationFormDto;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.UserServiceDownException;

/**
 * Responsible for registering new users.
 */
public interface UserService {

    /**Sends the new user's data to the user service to save them into the
     * database.
     * @param userDto Data transfer object with user data necessary to complete
     *                the registration process.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws UserServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    void registerNewUser(UserRegistrationFormDto userDto);

    /**Performs all the updates necessary when the user with a given username
     * logs in.
     * @param username Name of the user that just logged in.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws UserServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    void updateUserStatsOnLogin(String username);
}
