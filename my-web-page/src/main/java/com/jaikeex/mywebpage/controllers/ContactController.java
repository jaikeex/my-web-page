package com.jaikeex.mywebpage.controllers;

import com.jaikeex.mywebpage.dto.ContactFormDto;
import com.jaikeex.mywebpage.services.ContactService;
import com.jaikeex.mywebpage.utility.BindingResultErrorParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

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
        if (isResultOk(result, model)) {
            passEmailDataToContactService(contactFormDto, model);
            return CONTACT_SENDFORM_VIEW;
        }
        return CONTACT_VIEW;
    }

    private void passEmailDataToContactService(ContactFormDto contactFormDto, Model model) {
        try {
            contactService.sendContactFormAsEmail(contactFormDto);
            model.addAttribute(MESSAGE_SENT_ATTRIBUTE_NAME, true);
            model.addAttribute("message", "Message sent successfully!");
        } catch (HttpServerErrorException | HttpClientErrorException exception) {
            model.addAttribute(MESSAGE_SENT_ATTRIBUTE_NAME, false);
            model.addAttribute("message", exception.getResponseBodyAsString());
        }
    }

    private void addEmptyDtoIntoModel(Model model) {
        ContactFormDto contactFormDto = new ContactFormDto();
        model.addAttribute(CONTACT_FORM_DTO_ATTRIBUTE_NAME, contactFormDto);
    }

    private boolean isResultOk(BindingResult result, Model model) {
        BindingResultErrorParser errorParser = new BindingResultErrorParser
                .Builder(result, model).messageName(ERROR_MESSAGE_ATTRIBUTE_NAME).build();
        return errorParser.isResultOk();
    }
}
