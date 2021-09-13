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

}
