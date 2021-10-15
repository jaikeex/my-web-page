package com.jaikeex.mywebpage.mainwebsite.controller;

import com.jaikeex.mywebpage.mainwebsite.dto.EmailDto;
import com.jaikeex.mywebpage.mainwebsite.service.ContactService;
import com.jaikeex.mywebpage.mainwebsite.utility.BindingResultErrorParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.Valid;

@Controller
@Slf4j
public class ContactController {

    private static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";
    private static final String MESSAGE_SENT_ATTRIBUTE_NAME = "messageSent";
    private static final String CONTACT_VIEW = "contact";
    private static final String CONTACT_SENDFORM_VIEW = "contact/sendform";
    private static final String CONTACT_FORM_DTO_ATTRIBUTE_NAME = "emailDto";
    private static final String MESSAGE_SENT_STATUS_ATTRIBUTE_NAME = "message";
    private static final String MESSAGE_SENT_SUCCESSFULLY = "Message sent successfully!";

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping(value = "/contact")
    public String displayContactPage (Model model) {
        addEmptyDtoIntoModel(model);
        return CONTACT_VIEW;
    }

    @PostMapping(value = "/contact/sendform")
    public String postContactFormData(@Valid EmailDto emailDto, BindingResult result, Model model) {
        if (isResultOk(result, model)) {
            passEmailDataToContactService(emailDto, model);
            return CONTACT_SENDFORM_VIEW;
        }
        return CONTACT_VIEW;
    }

    private void passEmailDataToContactService(EmailDto emailDto, Model model) {
        contactService.sendEmailToAdmin(emailDto);
        appendModelWithMessageSentSuccessfullyAttributes(model);
    }

    private void addEmptyDtoIntoModel(Model model) {
        EmailDto emailDto = new EmailDto();
        model.addAttribute(CONTACT_FORM_DTO_ATTRIBUTE_NAME, emailDto);
    }

    private boolean isResultOk(BindingResult result, Model model) {
        BindingResultErrorParser errorParser = new BindingResultErrorParser
                .Builder(result, model).messageName(ERROR_MESSAGE_ATTRIBUTE_NAME).build();
        return errorParser.isResultOk();
    }

    private void appendModelWithEmailServiceErrorAttributes(Model model, HttpStatusCodeException exception) {
        String errorMessage = exception.getResponseBodyAsString();
        model.addAttribute(MESSAGE_SENT_ATTRIBUTE_NAME, false);
        log.debug("Added attribute to model [name={}, value={}]", MESSAGE_SENT_ATTRIBUTE_NAME, false);
        model.addAttribute(MESSAGE_SENT_STATUS_ATTRIBUTE_NAME, errorMessage);
        log.debug("Added attribute to model [name={}, value={}]", MESSAGE_SENT_STATUS_ATTRIBUTE_NAME, errorMessage);
    }

    private void appendModelWithMessageSentSuccessfullyAttributes(Model model) {
        model.addAttribute(MESSAGE_SENT_ATTRIBUTE_NAME, true);
        log.debug("Added attribute to model [name={}, value={}]", MESSAGE_SENT_ATTRIBUTE_NAME, true);
        model.addAttribute(MESSAGE_SENT_STATUS_ATTRIBUTE_NAME, MESSAGE_SENT_SUCCESSFULLY);
        log.debug("Added attribute to model [name={}, value={}]", MESSAGE_SENT_STATUS_ATTRIBUTE_NAME, MESSAGE_SENT_SUCCESSFULLY);
    }

    @ExceptionHandler({HttpServerErrorException.class, HttpClientErrorException.class})
    public String emailServiceError(Model model, HttpStatusCodeException exception) {
        appendModelWithEmailServiceErrorAttributes(model, exception);
        return CONTACT_SENDFORM_VIEW;
    }
}
