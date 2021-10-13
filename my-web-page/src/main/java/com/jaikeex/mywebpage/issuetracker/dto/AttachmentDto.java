package com.jaikeex.mywebpage.issuetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDto {
    private String title;
    private MultipartFile attachment;
}
