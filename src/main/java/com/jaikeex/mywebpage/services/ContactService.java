package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.ContactFormDto;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ContactService extends MyEmailService{

    /**
     * Constructs and sends me an email containing the message provided by the user.
     * @param contactFormDto Dto with the necessary data to complete the process.
     * @param model Model.
     * @return true if successful, false otherwise.
     */
    public boolean sendContactFormAsEmail(ContactFormDto contactFormDto, Model model) {
        this.setTo("hrubyy.jakub@gmail.com");
        String messageText = constructContactMessage(contactFormDto);
        if (sendMessage(contactFormDto.getSubject(), messageText)) {
            model.addAttribute("messageSent", true);
            model.addAttribute("message", "Message sent successfully!");
            return true;
        } else {
            model.addAttribute("messageSent", false);
            model.addAttribute("message", "There was an error sending your message");
            return false;
        }
    }

    private String constructContactMessage(ContactFormDto contactFormDto) {
        return contactFormDto.getMessageText() + "\n\n" + contactFormDto.getEmail();
    }

}
