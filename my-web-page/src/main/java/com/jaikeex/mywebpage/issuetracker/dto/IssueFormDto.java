package com.jaikeex.mywebpage.issuetracker.dto;

import com.jaikeex.mywebpage.issuetracker.model.properties.IssueType;
import com.jaikeex.mywebpage.issuetracker.model.properties.Project;
import com.jaikeex.mywebpage.issuetracker.model.properties.Severity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueFormDto {

    private String title;
    private String description;

    private IssueType type;
    private Severity severity;
    private Project project;

    private MultipartFile attachment;

}
