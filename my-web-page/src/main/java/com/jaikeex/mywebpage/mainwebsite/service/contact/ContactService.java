package com.jaikeex.mywebpage.mainwebsite.service.contact;

import com.jaikeex.mywebpage.mainwebsite.dto.EmailDto;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ContactServiceDownException;

/**
 * Responsible for sending the contact form messages as emails.
 */
public interface ContactService {

    /** Passes the data from a filled-in contact form to the email service
     * with an http request.
     * @param emailDto Data transfer object with the email form parameters
     *                       as its fields.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws ContactServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    void sendMessage(EmailDto emailDto);

}
