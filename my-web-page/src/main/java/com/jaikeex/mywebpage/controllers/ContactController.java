package com.jaikeex.mywebpage.controllers;

import com.jaikeex.mywebpage.dto.ContactFormDto;
import com.jaikeex.mywebpage.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ContactController {

    private static final String CONTACT_SENDFORM_ENDPOINT = "/contact/sendform";
    private static final String CONTACT_ENDPOINT = "/contact";
    private static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";
    private static final String MESSAGE_SENT_ATTRIBUTE_NAME = "messageSent";
    private static final String CONTACT_VIEW = "contact";
    private static final String CONTACT_SENDFORM_VIEW = "contact/sendform";
    private static final String CONTACT_FORM_DTO_ATTRIBUTE_NAME = "contactFormDto";

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping(value = CONTACT_ENDPOINT)
    public String contact (Model model) {
        addEmptyDtoIntoModel(model);
        return CONTACT_VIEW;
    }

    @PostMapping(value = CONTACT_SENDFORM_ENDPOINT)
    public String sendForm(@Valid ContactFormDto contactFormDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            parseBindingResultErrors(result, model);
            return CONTACT_VIEW;
        } else {
            contactService.sendContactFormAsEmail(contactFormDto, model);
            return CONTACT_SENDFORM_VIEW;
        }
    }

    private void addEmptyDtoIntoModel(Model model) {
        ContactFormDto contactFormDto = new ContactFormDto();
        model.addAttribute(CONTACT_FORM_DTO_ATTRIBUTE_NAME, contactFormDto);
    }

    private void parseBindingResultErrors(BindingResult result, Model model) {
        model.addAttribute(MESSAGE_SENT_ATTRIBUTE_NAME, false);
        for (ObjectError error : result.getAllErrors()) {
            if (error.toString().contains("Blank")) {
                model.addAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, "Please fill in all the fields!");
            }
            if (error.toString().contains("Pattern.email")) {
                model.addAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, "Wrong email!");
            }
        }
    }
}
