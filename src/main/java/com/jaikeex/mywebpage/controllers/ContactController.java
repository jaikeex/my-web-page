package com.jaikeex.mywebpage.controllers;

import com.jaikeex.mywebpage.dto.ContactFormDto;
import com.jaikeex.mywebpage.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ContactController {

    ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping(value = "/contact")
    public String contact (Model model) {
        ContactFormDto contactFormDto = new ContactFormDto();
        model.addAttribute("contactFormDto", contactFormDto);
        return "contact";
    }

    @PostMapping(value = "/contact/sendform")
    public String sendForm(Model model, @Valid ContactFormDto contactFormDto, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("messageSent", false);
            model.addAttribute("wrongEmailMessage", "Wrong email!");
            return "/contact";
        } else {
            contactService.sendContactFormAsEmail(contactFormDto, model);
        }
        return "/contact/sendform";
    }
}
