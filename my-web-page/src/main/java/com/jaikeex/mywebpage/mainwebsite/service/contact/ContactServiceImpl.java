package com.jaikeex.mywebpage.mainwebsite.service.contact;

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
public class ContactServiceImpl implements ContactService{

    private static final String CONTACT_EMAIL_RECIPIENT = "hrubyy.jakub@gmail.com";
    private static final Class<? extends ServiceDownException> SERVICE_EXCEPTION_TYPE = ContactServiceDownException.class;

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    private final ServiceRequest serviceRequest;

    @Autowired
    public ContactServiceImpl(MwpServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    @Override
    public void sendMessage(EmailDto emailDto) {
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
