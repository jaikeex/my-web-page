package com.jaikeex.mywebpage.issuetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentBytesDto {
    private String title;
    private byte[] attachmentBytes;
    private String originalFilename;
}
