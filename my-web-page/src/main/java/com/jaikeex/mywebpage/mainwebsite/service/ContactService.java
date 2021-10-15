package com.jaikeex.mywebpage.mainwebsite.service;

import com.jaikeex.mywebpage.mainwebsite.dto.EmailDto;
import com.jaikeex.mywebpage.mainwebsite.model.Email;
import com.jaikeex.mywebpage.resttemplate.RestTemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class ContactService{

    public static final String CONTACT_EMAIL_RECIPIENT = "hrubyy.jakub@gmail.com";

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    RestTemplateFactory restTemplateFactory;

    @Autowired
    public ContactService(RestTemplateFactory restTemplateFactory) {
        this.restTemplateFactory = restTemplateFactory;
    }

    /**Passes the data from a filled-in contact form to the email service
     * with an http request.
     * @param emailDto Data transfer object with the email form parameters
     *                       as its fields.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws org.springframework.web.client.HttpServerErrorException
     *          Whenever a 5xx http status code gets returned.
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
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        restTemplate.postForEntity(apiGatewayUrl + "emails/", email, Email.class);
        log.info("Sent a request to the email service with the following [email={}]", email);
    }
}
