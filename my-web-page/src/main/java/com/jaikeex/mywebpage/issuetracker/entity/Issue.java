package com.jaikeex.mywebpage.issuetracker.entity;

import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.issuetracker.entity.properties.IssueType;
import com.jaikeex.mywebpage.issuetracker.entity.properties.Project;
import com.jaikeex.mywebpage.issuetracker.entity.properties.Severity;
import com.jaikeex.mywebpage.issuetracker.entity.properties.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    public Issue(IssueDto issueDto) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        this.setDate(now);
        this.setStatus(Status.SUBMITTED);
        this.setAuthor(currentUserName);

        this.setTitle(issueDto.getTitle());
        this.setDescription(issueDto.getDescription());
        this.setType(issueDto.getType());
        this.setSeverity(issueDto.getSeverity());
        this.setProject(issueDto.getProject());
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
}


