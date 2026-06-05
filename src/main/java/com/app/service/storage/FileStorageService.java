package com.app.service.storage;
import com.app.validator.file.FileValidator;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
     String save(MultipartFile file, String disk, String folder, String fileName, FileValidator validator);
     void delete(String disk, String folder, String fileName);
     Resource loadAsResource(String disk, String folder, String fileName);
}
