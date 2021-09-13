package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.ContactFormDto;
import com.jaikeex.mywebpage.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

@Service
public class ContactService{

    RestTemplate restTemplate;

    @Autowired
    public ContactService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendContactFormAsEmail(ContactFormDto contactFormDto, Model model) {
        Email email = loadDataFromDtoIntoEmailObject(contactFormDto);
        try {
            sendMessage(email);
            model.addAttribute("messageSent", true);
            model.addAttribute("message", "Message sent successfully!");
        } catch (HttpServerErrorException exception){
            model.addAttribute("messageSent", false);
            model.addAttribute("message", exception.getResponseBodyAsString());
        }
    }

    private void sendMessage(Email email) {
        restTemplate.postForEntity(API_GATEWAY_URL + "emails/", email, Email.class);
    }

    private Email loadDataFromDtoIntoEmailObject(ContactFormDto contactFormDto) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Email email = new Email();
        email.setMessage(constructContactMessage(contactFormDto));
        email.setRecipient("hrubyy.jakub@gmail.com");
        email.setSubject(contactFormDto.getSubject());
        email.setDate(now);
        return email;
    }


    private String constructContactMessage(ContactFormDto contactFormDto) {
        return contactFormDto.getMessageText() + "\n\n" + contactFormDto.getEmail();
    }

}
