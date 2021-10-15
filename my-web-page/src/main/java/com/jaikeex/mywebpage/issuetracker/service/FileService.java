package com.jaikeex.mywebpage.issuetracker.service;

import com.jaikeex.mywebpage.config.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    private final Path issueAttachmentsFolder;

    @Autowired
    public FileService(StorageProperties properties) {
        this.issueAttachmentsFolder = Paths.get(properties.getIssueAttachmentsFolder());
    }

    public void save(MultipartFile attachment, String title) throws IOException {
        if (attachment.isEmpty()) {
            throw new RuntimeException("REXX");
        }
        String filePath = title + "/" + attachment.getOriginalFilename();
        Path destinationPath = issueAttachmentsFolder.resolve(
                Paths.get(filePath)).normalize().toAbsolutePath();
        try (InputStream inputStream = attachment.getInputStream()) {
            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
