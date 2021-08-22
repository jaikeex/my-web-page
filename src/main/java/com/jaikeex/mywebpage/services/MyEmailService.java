package com.jaikeex.mywebpage.services;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MyEmailService extends Authenticator{

    private String from = "kbhsmtp@gmail.com";
    private String host = "smtp.gmail.com";
    private String to;
    private Session session = null;

    public MyEmailService() {
    }

    protected PasswordAuthentication getPasswordAuthentication () {
        return new PasswordAuthentication("kbhsmtp@gmail.com", "heslo789");
    }

    private Properties constructProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.user", from);
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        return properties;
    }

    private MimeMessage constructMessage(Session session, String subject, String messageText) throws MessagingException{
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(messageText);
        return message;
    }

    public boolean sendMessage(String subject, String messageText) {
        Properties properties = constructProperties();
        if (session == null) {
            session = Session.getInstance(properties, this);
        }
        try {
            Transport.send(constructMessage(session, subject, messageText));
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
