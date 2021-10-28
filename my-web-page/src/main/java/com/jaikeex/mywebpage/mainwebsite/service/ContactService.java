package com.jaikeex.mywebpage.mainwebsite.service;

import com.jaikeex.mywebpage.config.connection.ServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.connection.MwpServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.dto.EmailDto;
import com.jaikeex.mywebpage.mainwebsite.model.Email;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ContactServiceDownException;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ContactService{

    private static final String CONTACT_EMAIL_RECIPIENT = "hrubyy.jakub@gmail.com";
    private static final Class<? extends ServiceDownException> SERVICE_EXCEPTION_TYPE = ContactServiceDownException.class;

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    private final ServiceRequest serviceRequest;

    @Autowired
    public ContactService(MwpServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    /**Passes the data from a filled-in contact form to the email service
     * with an http request.
     * @param emailDto Data transfer object with the email form parameters
     *                       as its fields.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws ContactServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    public void sendEmailToAdmin(EmailDto emailDto) {
        Email email = loadDataFromDtoIntoEmailObject(emailDto);
        postHttpRequestToEmailService(email);
    }

    private Email loadDataFromDtoIntoEmailObject(EmailDto emailDto) {
        return new Email.Builder(CONTACT_EMAIL_RECIPIENT).subject(emailDto.getSubject())
                .message(constructContactMessage(emailDto)).build();
    }

    private String constructContactMessage(EmailDto emailDto) {
        return emailDto.getMessageText() + "\n\nAuthor: " + emailDto.getSender();
    }

    private void postHttpRequestToEmailService(Email email) {
        String url = apiGatewayUrl + "emails/";
        serviceRequest.sendPostRequest(url, email, SERVICE_EXCEPTION_TYPE);
        log.info("Sent a request to the email service with the following [email={}]", email);
    }
}
