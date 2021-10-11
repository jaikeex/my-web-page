package com.jaikeex.mywebpage.mainwebsite.controllers;

import com.jaikeex.mywebpage.mainwebsite.dto.EmailDto;
import com.jaikeex.mywebpage.mainwebsite.services.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedList;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    public static final String CONTACT_VIEW_ID_COMMENT = "This is the contact view";
    public static final String CONTACT_SENDFORM_VIEW_ID_COMMENT = "This is the contact/sendform view";
    public static final String CONTACT_FORM_DTO_ATTRIBUTE_NAME = "contactFormDto";
    public static final String CONTACT_SENDFORM_ENDPOINT = "/contact/sendform";
    public static final String CONTACT_ENDPOINT = "/contact";


    @MockBean
    ContactService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    private final EmailDto emailDto = new EmailDto(
            "bar@example.com",
            "test subject",
            "test body");

    private final EmailDto emptyEmailDto = new EmailDto();


    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void contact_shouldAddDtoToModel() throws Exception {
        mockMvc.perform(get(CONTACT_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(CONTACT_FORM_DTO_ATTRIBUTE_NAME));
    }

    @Test
    public void contact_shouldReturnCorrectView() throws Exception {
        mockMvc.perform(get(CONTACT_ENDPOINT))
                .andExpect(content().string(containsString(CONTACT_VIEW_ID_COMMENT)));
    }

    @Test
    public void sendForm_shouldSendConfirmationEmail() throws Exception {
        postContactFormDtoToSendformEndpoint(emailDto)
                .andExpect(status().isOk());
        verify(service).sendEmailToAdmin(any(EmailDto.class));
    }

    @Test
    public void sendForm_shouldFailWithInvalidEmail() throws Exception {
        emailDto.setSender("bar@example");
        postContactFormDtoToSendformEndpoint(emailDto)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Wrong email")));
    }

    @Test
    public void sendForm_shouldFailWithNoMessage() throws Exception {
        emailDto.setMessageText("");
        postContactFormDtoToSendformEndpoint(emailDto)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please fill in")));
    }

    @Test
    public void sendForm_shouldFailWithNoSubject() throws Exception {
        emailDto.setSubject("");
        postContactFormDtoToSendformEndpoint(emailDto)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please fill in")));
    }

    @Test
    public void sendForm_givenResultError_shouldReturnCorrectView() throws Exception {
        getBindingResultMock();
        postContactFormDtoToSendformEndpoint(emptyEmailDto)
                .andExpect(content().string(containsString(CONTACT_VIEW_ID_COMMENT)));
    }

    @Test
    public void sendForm_givenNoErrors_shouldReturnCorrectView() throws Exception {
        getBindingResultMock();
        postContactFormDtoToSendformEndpoint(emailDto)
                .andExpect(content().string(containsString(CONTACT_SENDFORM_VIEW_ID_COMMENT)));
    }

    @Test
    public void sendForm_shouldCatchHttpServerErrorException() throws Exception {
        HttpServerErrorException exception =
                new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "testException");
        doThrow(exception).when(service).sendEmailToAdmin(emailDto);
        postContactFormDtoToSendformEndpoint(emailDto)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("message"));
    }

    @Test
    public void sendForm_shouldCatchHttpClientErrorException() throws Exception {
        HttpClientErrorException exception =
                new HttpClientErrorException(HttpStatus.BAD_REQUEST, "testException");
        doThrow(exception).when(service).sendEmailToAdmin(emailDto);
        postContactFormDtoToSendformEndpoint(emailDto)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("message"));
    }

    private void getBindingResultMock() {
        BindingResult resultMock = mock(BindingResult.class);
        when(resultMock.hasErrors()).thenReturn(true);
        when(resultMock.getAllErrors()).thenReturn(new LinkedList<>());

    }

    private ResultActions postContactFormDtoToSendformEndpoint(EmailDto emailDto) throws Exception {
        return mockMvc.perform(post(CONTACT_SENDFORM_ENDPOINT)
                .with(csrf())
                .flashAttr(CONTACT_FORM_DTO_ATTRIBUTE_NAME, emailDto));
    }


}