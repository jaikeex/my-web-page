package com.jaikeex.mywebpage.issuetracker.entity;

import com.jaikeex.mywebpage.issuetracker.dto.IssueFormDto;
import com.jaikeex.mywebpage.issuetracker.entity.properties.IssueType;
import com.jaikeex.mywebpage.issuetracker.entity.properties.Project;
import com.jaikeex.mywebpage.issuetracker.entity.properties.Severity;
import com.jaikeex.mywebpage.issuetracker.entity.properties.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Issue {

    public Issue(IssueFormDto issueFormDto) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        this.setDate(now);
        this.setStatus(Status.SUBMITTED);
        this.setAuthor(currentUserName);
        this.setTitle(issueFormDto.getTitle());
        this.setDescription(issueFormDto.getDescription());
        this.setType(issueFormDto.getType());
        this.setSeverity(issueFormDto.getSeverity());
        this.setProject(issueFormDto.getProject());
        log.debug("Initialized new Issue from IssueFormDto [title={}, author={}]", title, author);
    }

    private int id;
    private String title;
    private String description;

    private Timestamp date;
    private String author;

    private IssueType type;
    private Severity severity;
    private Status status;
    private Project project;

    private List<HistoryRecord> historyRecords;
    private List<Attachment> attachments;
}


