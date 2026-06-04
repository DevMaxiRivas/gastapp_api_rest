package com.app.service.profile;

import com.app.dto.v1.profile.ProfileCreateDTO;
import com.app.dto.v1.profile.ProfileResponseDTO;
import com.app.dto.v1.profile.ProfileUpdateDTO;
import com.app.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    ProfileResponseDTO create(MultipartFile avatarImage, ProfileCreateDTO dto, User user);
    ProfileResponseDTO update(MultipartFile avatarImage, ProfileUpdateDTO dto, User user);
}
