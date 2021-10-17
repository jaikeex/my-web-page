package com.jaikeex.mywebpage.issuetracker.dto;

import com.jaikeex.mywebpage.issuetracker.model.properties.IssueType;
import com.jaikeex.mywebpage.issuetracker.model.properties.Project;
import com.jaikeex.mywebpage.issuetracker.model.properties.Severity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class IssueDto {

    private String title;
    private String description;
    private String author;

    private IssueType type;
    private Severity severity;
    private Project project;

    private AttachmentFileDto attachmentFileDto;

    public IssueDto(IssueFormDto issueFormDto) throws IOException {
        this.setTitle(issueFormDto.getTitle());
        this.setDescription(issueFormDto.getDescription());
        this.setType(issueFormDto.getType());
        this.setSeverity(issueFormDto.getSeverity());
        this.setProject(issueFormDto.getProject());
        this.setAuthorAsCurrentPrincipal();
        this.attachmentFileDto = new AttachmentFileDto(issueFormDto.getAttachment(), issueFormDto.getTitle());
        log.debug("Initialized IssueDto from IssueFormDto [issueDto={}]", this);
    }

    private void setAuthorAsCurrentPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.author = authentication.getName();
    }
}
