package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.ContactFormDto;
import com.jaikeex.mywebpage.model.Email;
import com.jaikeex.mywebpage.restemplate.RestTemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

@Service
@Slf4j
public class ContactService{

    public static final String CONTACT_EMAIL_RECIPIENT = "hrubyy.jakub@gmail.com";

    RestTemplateFactory restTemplateFactory;

    @Autowired
    public ContactService(RestTemplateFactory restTemplateFactory) {
        this.restTemplateFactory = restTemplateFactory;
    }

    /**Passes the data from a filled-in contact form to the email service
     * with an http request.
     * @param contactFormDto Data transfer object with the email form parameters
     *                       as its fields.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws org.springframework.web.client.HttpServerErrorException
     *          Whenever a 5xx http status code gets returned.
     */
    public void sendContactFormAsEmail(ContactFormDto contactFormDto) {
        Email email = loadDataFromDtoIntoEmailObject(contactFormDto);
        postHttpRequestToEmailService(email);
    }

    private Email loadDataFromDtoIntoEmailObject(ContactFormDto contactFormDto) {
        return new Email.Builder(CONTACT_EMAIL_RECIPIENT).subject(contactFormDto.getSubject())
                .message(constructContactMessage(contactFormDto)).build();
    }

    private String constructContactMessage(ContactFormDto contactFormDto) {
        return contactFormDto.getMessageText() + "\n\n" + contactFormDto.getEmail();

    }

    private void postHttpRequestToEmailService(Email email) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        restTemplate.postForEntity(API_GATEWAY_URL + "emails/", email, Email.class);
        log.info("Sent a request to the email service with the following [email={}]", email);
    }
}
