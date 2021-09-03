package com.jaikeex.mywebpage.services;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.sun.mail.imap.IMAPStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.TestComponent;
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

    String subject = "test subject";
    String body = "test body";

    @Test
    public void testSendPlainMessage() throws MessagingException, IOException {
        MyEmailService service = new MyEmailService();
        Session smtpSession = greenMail.getSmtp().createSession();
        service.setTo("bar@example.com");
        service.setFrom("foo@example.com");
        service.setSession(smtpSession);
        service.sendMessage(subject, body);

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