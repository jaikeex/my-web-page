package com.jaikeex.mywebpage.mainwebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private String name;
    private String githubLink;
    private String introduction;
    private String detailedDescription;
    private String language;
    private String snapshot;

}
