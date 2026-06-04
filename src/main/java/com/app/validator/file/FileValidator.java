package com.app.validator.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileValidator {
    void validate(MultipartFile file);
}
