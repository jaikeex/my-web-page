package com.jaikeex.mywebpage.mainwebsite.services;


import com.jaikeex.mywebpage.mainwebsite.dto.ContactFormDto;
import com.jaikeex.mywebpage.mainwebsite.model.Email;
import com.jaikeex.mywebpage.resttemplate.RestTemplateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestComponent
@ExtendWith(SpringExtension.class)
class ContactServiceTest {

    public static final String TEST_SUBJECT = "testSubject";
    public static final String TEST_MESSAGE_TEXT = "testMessageText";
    public static final String TEST_EMAIL = "testEmail";
    ContactFormDto testContactFormDto = new ContactFormDto();

    @Mock
    RestTemplateFactory restTemplateFactory;
    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    ContactService service;

    @BeforeEach
    public void beforeEach() {
        when(restTemplateFactory.getRestTemplate()).thenReturn(restTemplate);
        testContactFormDto.setSubject(TEST_SUBJECT);
        testContactFormDto.setMessageText(TEST_MESSAGE_TEXT);
        testContactFormDto.setEmail(TEST_EMAIL);
    }

    @Test
    public void sendContactFormAsEmail_emailShouldIncludeSubject() {
        ArgumentCaptor<Email> argument = ArgumentCaptor.forClass(Email.class);
        service.sendContactFormAsEmail(testContactFormDto);
        verify(restTemplate).postForEntity(anyString(), argument.capture(), any());
        assertTrue(argument.getValue().getSubject().contains(TEST_SUBJECT));
    }

    @Test
    public void sendContactFormAsEmail_emailShouldIncludeBody() {
        ArgumentCaptor<Email> argument = ArgumentCaptor.forClass(Email.class);
        service.sendContactFormAsEmail(testContactFormDto);
        verify(restTemplate).postForEntity(anyString(), argument.capture(), any());
        assertTrue(argument.getValue().getMessage().contains(TEST_MESSAGE_TEXT));
    }

    @Test
    public void sendContactFormAsEmail_emailShouldIncludeEmailAddress() {
        ArgumentCaptor<Email> argument = ArgumentCaptor.forClass(Email.class);
        service.sendContactFormAsEmail(testContactFormDto);
        verify(restTemplate).postForEntity(anyString(), argument.capture(), any());
        assertTrue(argument.getValue().getMessage().contains(TEST_EMAIL));
    }

    @Test
    public void sendContactFormAsEmail_shouldPostHttpRequestToEmailService() {
        service.sendContactFormAsEmail(testContactFormDto);
        verify(restTemplate, times(1))
                .postForEntity(anyString(), any(Email.class), any());
    }

}