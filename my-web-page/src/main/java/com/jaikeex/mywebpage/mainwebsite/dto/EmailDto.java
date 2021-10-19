package com.jaikeex.mywebpage.mainwebsite.dto;

import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.mainwebsite.utility.validators.ValidateEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class EmailDto {

    @ValidateEmail
    private String sender;
    @NotBlank
    private String subject;
    @NotBlank
    private String messageText;

    public EmailDto(IssueDto issueDto) {
        this.setSender(issueDto.getAuthor());
        this.setSubject("New report! - " + issueDto.getTitle());
        this.setMessageText(issueDto.getDescription());
        log.debug("Initialized EmailDto from IssueDto [emailDto={}]", this);
    }
}
