package com.jaikeex.mywebpage.issuetracker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRecord {

    private int id;
    private String text;
    private Timestamp date;
    private Issue issue;

}
