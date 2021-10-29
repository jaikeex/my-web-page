package com.jaikeex.emailservice.service;

import com.jaikeex.emailservice.entity.Email;
import com.jaikeex.emailservice.repository.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.sun.mail.imap.IMAPStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestComponent
@ExtendWith(SpringExtension.class)
class MyEmailServiceTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP_IMAP);

    @Mock
    EmailRepository repository;

    @InjectMocks
    MyEmailService service;

    String subject = "test subject";
    String body = "test body";

    Email email;

    @BeforeEach
    public void beforeEach() {
        email = new Email(subject, body);
        email.setRecipient("bar@example.com");
    }

    @Test
    public void testSendPlainMessage() throws MessagingException, IOException {
        Session smtpSession = greenMail.getSmtp().createSession();
        service.setTo("bar@example.com");
        service.setFrom("foo@example.com");
        service.setSession(smtpSession);
        service.sendMessage(email);

        greenMail.setUser("bar@example.com", "bar@example.com", "password");
        IMAPStore store = greenMail.getImap().createStore();
        store.connect("bar@example.com", "password");
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        Message msgReceived = inbox.getMessage(1);
        assertTrue(msgReceived.getSubject().contains(subject));
        assertTrue(msgReceived.getContent().toString().contains(body));
    }
}