package com.app.mapper.helper;

import com.app.mapper.qualifier.storage.GetPublicPath;
import com.app.service.storage.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StorageMapper {
    private final FileStorageService fileStorageService;

    @GetPublicPath
    public String generateURLPublic(String uri) {
        return fileStorageService.generateURLPublic(uri);
    }
}
