package com.jaikeex.mywebpage.issuetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentFileDto {

    public AttachmentFileDto(AttachmentFormDto attachmentFormDto) throws IOException {
        this.setIssueTitle(attachmentFormDto.getTitle());
        this.setBytes(attachmentFormDto.getAttachment().getBytes());
        this.setOriginalFilename(attachmentFormDto.getAttachment().getOriginalFilename());
    }

    public AttachmentFileDto(MultipartFile file, String issueTitle) throws IOException {
        this.setIssueTitle(issueTitle);
        this.setOriginalFilename(file.getOriginalFilename());
        this.setBytes(file.getBytes());
    }

    private String issueTitle;
    private byte[] bytes;
    private String originalFilename;
}
