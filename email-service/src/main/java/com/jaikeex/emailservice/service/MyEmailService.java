package com.jaikeex.emailservice.service;

import com.jaikeex.emailservice.entity.Email;
import com.jaikeex.emailservice.repository.EmailRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MyEmailService extends Authenticator {

    private static final String SMTP_USERNAME = "kbhsmtp@gmail.com";
    private static final String SMTP_PASSWORD = "redacted";
    EmailRepository repository;

    private String from = "kbhsmtp@gmail.com";
    private String host = "smtp.gmail.com";
    private String to;
    private Session session = null;

    @Autowired
    public MyEmailService(EmailRepository repository) {
        this.repository = repository;
    }

    public void sendMessage(Email email) throws MessagingException {
        this.to = email.getRecipient();
        setActiveSession();
        MimeMessage message = constructMessage(email.getSubject(), email.getMessage());
        Transport.send(message, SMTP_USERNAME, SMTP_PASSWORD);
        repository.save(email);
    }

    protected PasswordAuthentication getPasswordAuthentication () {
        return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
    }

    private void setActiveSession() {
        if (session == null) {
            Properties properties = constructProperties();
            setSession(Session.getInstance(properties));
        }
    }

    private Properties constructProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.user", from);
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        return properties;
    }

    private MimeMessage constructMessage(String subject, String messageBody) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }
}
