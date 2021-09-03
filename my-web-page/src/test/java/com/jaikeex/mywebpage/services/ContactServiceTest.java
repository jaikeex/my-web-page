package com.jaikeex.mywebpage.services;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.jaikeex.mywebpage.dto.ContactFormDto;
import com.sun.mail.imap.IMAPStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestComponent
@ExtendWith(SpringExtension.class)
class ContactServiceTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP_IMAP);

    private final ContactFormDto contactFormDto = new ContactFormDto(
            "bar@example.com",
            "test subject",
            "test body");

    ContactService service = new ContactService();

    @Test
    public void testSendConfirmationEmailContainsHeader() throws MessagingException, IOException {
        Model model = new ExtendedModelMap();
        Session smtpSession = greenMail.getSmtp().createSession();
        service.setTo("bar@example.com");
        service.setFrom("foo@example.com");
        service.setSession(smtpSession);
        service.sendContactFormAsEmail(contactFormDto, model);

        greenMail.setUser("hrubyy.jakub@gmail.com", "hrubyy.jakub@gmail.com", "password");
        IMAPStore store = greenMail.getImap().createStore();
        store.connect("hrubyy.jakub@gmail.com", "password");
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        Message msgReceived = inbox.getMessage(1);
        assertTrue(msgReceived.getContent().toString().contains("test body"));
    }
}