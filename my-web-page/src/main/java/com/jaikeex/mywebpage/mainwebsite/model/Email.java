package com.jaikeex.mywebpage.mainwebsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Email {

    private Integer id;
    private String subject;
    private String message;
    private String recipient;
    private Timestamp date;

    private Email(Builder builder) {
        this.subject = builder.subject;
        this.message = builder.message;
        this.recipient = builder.recipient;
        this.date = builder.date;
    }


    public static class Builder {

        private final String recipient;

        private String subject = "";
        private String message = "";
        private Timestamp date = new Timestamp(System.currentTimeMillis());

        public Builder(String recipient) {
            this.recipient = recipient;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder date(Timestamp date) {
            this.date = date;
            return this;
        }

        public Email build() {
            Email email = new Email(this);
            log.debug("Building new Email [email={}]", email);
            return email;
        }
    }

    @Override
    public String toString() {
        return "Email{" +
                "subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}

