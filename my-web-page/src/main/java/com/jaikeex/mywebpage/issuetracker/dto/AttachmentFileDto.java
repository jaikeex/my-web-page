package com.jaikeex.mywebpage.issuetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class AttachmentFileDto {

    private String issueTitle;
    private byte[] bytes;
    private String originalFilename;

    public AttachmentFileDto(AttachmentFormDto attachmentFormDto) throws IOException {
        this.setIssueTitle(attachmentFormDto.getTitle());
        this.setBytes(attachmentFormDto.getAttachment().getBytes());
        this.setOriginalFilename(attachmentFormDto.getAttachment().getOriginalFilename());
        log.debug("Initialized AttachmentFileDto from AttachmentFormDto [attachmentFileDto={}]", this);
    }

    public AttachmentFileDto(MultipartFile file, String issueTitle) throws IOException {
        this.setIssueTitle(issueTitle);
        this.setBytes(file.getBytes());
        this.setOriginalFilename(file.getOriginalFilename());
        log.debug("Initialized AttachmentFileDto from MultipartFile [attachmentFileDto={}]", this);
    }
}
