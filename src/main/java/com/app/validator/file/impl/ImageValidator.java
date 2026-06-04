package com.app.validator.file.impl;

import com.app.exception.storage.SaveFileCustomException;
import com.app.validator.file.FileValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class ImageValidator implements FileValidator {
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png");

    @Override
    public void validate(MultipartFile file) {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (extension == null || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new SaveFileCustomException(new IOException("Not allowed image format. Use: " + ALLOWED_EXTENSIONS));
        }
    }
}