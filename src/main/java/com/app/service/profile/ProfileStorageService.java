package com.app.service.profile;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileStorageService {
    String saveAvatar(MultipartFile avatar, String disk, String directory , String fileName);
}
