package com.jaikeex.mywebpage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

        private String recipient;

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

        public Builder message(Timestamp date) {
            this.date = date;
            return this;
        }

        public Email build() {
            return new Email(this);
        }
    }
}

