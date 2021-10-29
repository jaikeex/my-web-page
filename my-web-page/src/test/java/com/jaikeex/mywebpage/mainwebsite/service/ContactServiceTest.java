package com.jaikeex.mywebpage.mainwebsite.service;


import com.jaikeex.mywebpage.mainwebsite.connection.MwpServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.dto.EmailDto;
import com.jaikeex.mywebpage.mainwebsite.model.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    EmailDto testEmailDto = new EmailDto();

    @Mock
    MwpServiceRequest serviceRequest;

    @InjectMocks
    ContactService service;

    @BeforeEach
    public void beforeEach() {
        testEmailDto.setSubject(TEST_SUBJECT);
        testEmailDto.setMessageText(TEST_MESSAGE_TEXT);
        testEmailDto.setSender(TEST_EMAIL);
    }

    @Test
    public void sendContactFormAsEmail_emailShouldIncludeSubject() {
        ArgumentCaptor<Email> argument = ArgumentCaptor.forClass(Email.class);
        service.sendEmailToAdmin(testEmailDto);
        verify(serviceRequest).sendPostRequest(anyString(), argument.capture(), any());
        assertTrue(argument.getValue().getSubject().contains(TEST_SUBJECT));
    }

    @Test
    public void sendContactFormAsEmail_emailShouldIncludeBody() {
        ArgumentCaptor<Email> argument = ArgumentCaptor.forClass(Email.class);
        service.sendEmailToAdmin(testEmailDto);
        verify(serviceRequest).sendPostRequest(anyString(), argument.capture(), any());
        assertTrue(argument.getValue().getMessage().contains(TEST_MESSAGE_TEXT));
    }

    @Test
    public void sendContactFormAsEmail_emailShouldIncludeEmailAddress() {
        ArgumentCaptor<Email> argument = ArgumentCaptor.forClass(Email.class);
        service.sendEmailToAdmin(testEmailDto);
        verify(serviceRequest).sendPostRequest(anyString(), argument.capture(), any());
        assertTrue(argument.getValue().getMessage().contains(TEST_EMAIL));
    }

    @Test
    public void sendContactFormAsEmail_shouldPostHttpRequestToEmailService() {
        service.sendEmailToAdmin(testEmailDto);
        verify(serviceRequest, times(1))
                .sendPostRequest(anyString(), any(Email.class), any());
    }

}