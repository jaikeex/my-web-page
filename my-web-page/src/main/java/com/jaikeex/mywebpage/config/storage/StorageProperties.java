package com.jaikeex.mywebpage.config.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "storage.folder")
@EnableConfigurationProperties({StorageProperties.class})
public class StorageProperties {

    private String issueAttachmentsFolder;
}
