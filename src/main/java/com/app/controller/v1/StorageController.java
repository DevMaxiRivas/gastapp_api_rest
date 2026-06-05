package com.app.controller.v1;

import com.app.service.storage.FileStorageService;
import lombok.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {
    private final FileStorageService storageService;

    private ResponseEntity<Resource> serveFile(String disk, String folder, String fileName) {
        Resource file = storageService.loadAsResource(disk, folder, fileName);

        String contentType = null;
        try {
            contentType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/public/{resource}/{folder}/{fileName:.+}")
    public ResponseEntity<Resource> getPublicFile(
            @PathVariable String resource,
            @PathVariable String folder,
            @PathVariable String fileName
    ) {
        String fullFolder = resource + "/" + folder;
        return serveFile("public", fullFolder, fileName);
    }

    @GetMapping("/private/{folder}/{fileName:.+}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> getPrivateFile(
            @PathVariable String folder,
            @PathVariable String fileName
    ) {
        return serveFile("private", folder, fileName);
    }
}
