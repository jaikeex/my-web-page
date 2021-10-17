package com.jaikeex.mywebpage.issuetracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    private int id;
    private String path;
    private Timestamp date;
    private String originalFilename;
    private Issue issue;

}
