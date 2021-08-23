package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.dto.ContactFormDto;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ContactService extends MyEmailService{


    public boolean sendContactFormAsEmail(ContactFormDto contactFormDto, Model model) {
        this.setTo("hrubyy.jakub@gmail.com");

        String messageText = contactFormDto.getMessageText() + "\n\n" + contactFormDto.getEmail();

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



}
