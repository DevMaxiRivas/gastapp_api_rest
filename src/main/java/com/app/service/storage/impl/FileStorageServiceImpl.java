package com.app.service.storage.impl;

import com.app.exception.resource.ResourceNotFoundCustomException;
import com.app.exception.storage.SaveFileCustomException;
import com.app.service.storage.FileStorageService;
import com.app.validator.file.FileValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${spring.application.storage.public-path}")
    private String publicRoot;

    @Value("${spring.application.storage.public-path}")
    private String privateRoot;

    private Path determineStoragePath(String disk){
        return "public".equals(disk) ? Paths.get(publicRoot) : Paths.get(privateRoot);
    }

    public String save(MultipartFile file, String disk, String folder, String fileName, FileValidator validator) {
        validator.validate(file);
        Path baseDir = determineStoragePath(disk);
        Path targetDir = baseDir.resolve(folder);
        try {
            Files.createDirectories(targetDir);

            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String finalName = fileName + "_" + System.currentTimeMillis() + "." + extension;

            Files.copy(file.getInputStream(), targetDir.resolve(finalName), StandardCopyOption.REPLACE_EXISTING);

            return finalName;
        } catch (IOException e) {
            log.error("Error saving the file to {}: {}", targetDir, e.getMessage());
            throw new SaveFileCustomException(e);
        }
    }

    public void delete(String disk, String folder, String fileName) {
        if (fileName == null || fileName.isEmpty()) return;

        Path baseDir = determineStoragePath(disk);
        Path filePath = baseDir.resolve(folder).resolve(fileName);

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("The file could not be deleted: {}", filePath);
        }
    }

    public Resource loadAsResource(String disk, String folder, String fileName) {
        try {
            Path baseDir = determineStoragePath(disk);
            Path filePath = baseDir.resolve(folder).resolve(fileName);

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundCustomException("Unable to read the file: " + fileName, "url");
            }
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundCustomException("The file was not found: " + fileName, "url");
        }
    }

}