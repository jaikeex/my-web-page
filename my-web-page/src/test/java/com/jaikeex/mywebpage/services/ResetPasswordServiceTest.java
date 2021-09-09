package com.jaikeex.mywebpage.services;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.jaikeex.mywebpage.dto.ResetPasswordDto;
import com.jaikeex.mywebpage.entity.User;
import com.jaikeex.mywebpage.jpa.UserRepository;
import com.jaikeex.mywebpage.services.security.MyPasswordEncoder;
import com.sun.mail.imap.IMAPStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestComponent
@ExtendWith(SpringExtension.class)
class ResetPasswordServiceTest {
    private final User testUser1 = new User(
            1,
            "testuserfordbaccess",
            "$argon2id$v=19$m=65536,t=3,p=1$peMkKGWTfioAQols1mso3A$dG2V75p0v6onSrFT9kOtMqhwmqOCsySt6la1QYtH2Jc",
            "testuserfordbaccess@testuserfordbaccess.com",
            "$argon2id$v=19$m=65536,t=3,p=1$ZRzpyukFgnnO1m6bkadOGA$2RxS99w4CBZVGQ/vXy8TMbr7VvXcifba2FCJePEyA/4",
            null,
            null,
            null,
            true,
            "USER");

    private final ResetPasswordDto resetPasswordDto = new ResetPasswordDto(
            "testuserfordbaccess@testuserfordbaccess.com",
            "rCkE4HajtcoG2PIRpZk2qsYt5ZJwl1gU",
            "testuserfordbaccess",
            "testuserfordbaccess");


    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP_IMAP);

    @Mock
    UserRepository repository;
    @Mock
    MyPasswordEncoder encoder;

    @InjectMocks
    ResetPasswordService resetPasswordService;

    @Test
    public void testResetPasswordWithValidInput() {
        when(repository.findByEmail(resetPasswordDto.getEmail())).thenReturn(testUser1);
        when(encoder.matches(resetPasswordDto.getToken(),testUser1.getResetPasswordToken())).thenReturn(true);
        boolean actual = resetPasswordService.resetPassword(resetPasswordDto);
        assertTrue(actual);
    }

    @Test
    public void testResetPasswordWithInvalidToken() {
        when(repository.findByEmail(resetPasswordDto.getEmail())).thenReturn(testUser1);
        when(encoder.matches(resetPasswordDto.getToken(),testUser1.getResetPasswordToken())).thenReturn(false);
        boolean actual = resetPasswordService.resetPassword(resetPasswordDto);
        assertFalse(actual);
    }

    @Test
    public void testResetPasswordWithNoSuchUserInDatabase() {
        when(repository.findByEmail(resetPasswordDto.getEmail())).thenReturn(null);
        when(encoder.matches(resetPasswordDto.getToken(),testUser1.getResetPasswordToken())).thenReturn(false);
        boolean actual = resetPasswordService.resetPassword(resetPasswordDto);
        assertFalse(actual);
    }

    @Test
    public void testSendConfirmationEmailContainsLink() throws MessagingException, IOException {
        when(repository.findByEmail("bar@example.com")).thenReturn(testUser1);

        Session smtpSession = greenMail.getSmtp().createSession();
        resetPasswordService.setTo("bar@example.com");
        resetPasswordService.setFrom("foo@example.com");
        resetPasswordService.setSession(smtpSession);
        resetPasswordService.sendConfirmationEmail("bar@example.com");

        greenMail.setUser("bar@example.com", "bar@example.com", "password");
        IMAPStore store = greenMail.getImap().createStore();
        store.connect("bar@example.com", "password");
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        Message msgReceived = inbox.getMessage(1);
        assertTrue(msgReceived.getContent().toString().contains("http://localhost:8080/user/reset-password?token="));
    }

    @Test
    public void testSendConfirmationEmailContainsHeader() throws MessagingException, IOException {
        when(repository.findByEmail("bar@example.com")).thenReturn(testUser1);

        Session smtpSession = greenMail.getSmtp().createSession();
        resetPasswordService.setTo("bar@example.com");
        resetPasswordService.setFrom("foo@example.com");
        resetPasswordService.setSession(smtpSession);
        resetPasswordService.sendConfirmationEmail("bar@example.com");

        greenMail.setUser("bar@example.com", "bar@example.com", "password");
        IMAPStore store = greenMail.getImap().createStore();
        store.connect("bar@example.com", "password");
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        Message msgReceived = inbox.getMessage(1);
        assertTrue(msgReceived.getContent().toString().contains("You requested to reset your password for www.kubahruby.com"));
    }

    @Test
    public void testSendConfirmationEmailContainsFooter() throws MessagingException, IOException {
        when(repository.findByEmail("bar@example.com")).thenReturn(testUser1);

        Session smtpSession = greenMail.getSmtp().createSession();
        resetPasswordService.setTo("bar@example.com");
        resetPasswordService.setFrom("foo@example.com");
        resetPasswordService.setSession(smtpSession);
        resetPasswordService.sendConfirmationEmail("bar@example.com");

        greenMail.setUser("bar@example.com", "bar@example.com", "password");
        IMAPStore store = greenMail.getImap().createStore();
        store.connect("bar@example.com", "password");
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        Message msgReceived = inbox.getMessage(1);
        assertTrue(msgReceived.getContent().toString().contains("Best wishes, Jakub Hrubý."));
    }

    @Test
    public void testSendConfirmationEmailWithNoSuchUserInDatabase() throws MessagingException{
        when(repository.findByEmail("bar@example.com")).thenReturn(null);

        Session smtpSession = greenMail.getSmtp().createSession();
        resetPasswordService.setTo("bar@example.com");
        resetPasswordService.setFrom("foo@example.com");
        resetPasswordService.setSession(smtpSession);
        resetPasswordService.sendConfirmationEmail("bar@example.com");

        greenMail.setUser("bar@example.com", "bar@example.com", "password");
        IMAPStore store = greenMail.getImap().createStore();
        store.connect("bar@example.com", "password");
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        assertEquals(0, inbox.getMessageCount());
    }
}