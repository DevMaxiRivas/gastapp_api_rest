package com.app.service.profile.impl;

import com.app.dto.v1.profile.ProfileCreateDTO;
import com.app.dto.v1.profile.ProfileResponseDTO;
import com.app.dto.v1.profile.ProfileUpdateDTO;
import com.app.exception.app.profile.ProfileAlreadyCreatedException;
import com.app.mapper.profile.ProfileMapper;
import com.app.model.Profile;
import com.app.model.User;
import com.app.repository.ProfileRepository;
import com.app.service.profile.ProfileService;
import com.app.service.storage.FileStorageService;
import com.app.service.storage.impl.FileStorageServiceImpl;
import com.app.validator.file.impl.ImageValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repo;
    private final ProfileMapper mapper;
    private final FileStorageService storageService;
    private final ImageValidator imageValidator;

    private static final String AVATAR_FOLDER = "profiles/avatars";

    @Transactional
    @Override
    public ProfileResponseDTO create(MultipartFile avatarImage, ProfileCreateDTO dto, User user) {
        if(user.getProfile() != null) {
            throw new ProfileAlreadyCreatedException();
        }

        Profile profile = Profile.builder()
                .user_id(user.getId())
                .currency(dto.getCurrency())
                .currentBudget(dto.getCurrentBudget())
                .build();

        if (avatarImage != null) {
            String newFileName = storageService.save(
                    avatarImage,
                    "public",
                    AVATAR_FOLDER,
                    user.getId().toString(),
                    imageValidator
            );

            profile.setAvatarUrl(AVATAR_FOLDER + "/" + newFileName);
        }

        repo.save(profile);
        System.out.println("Avatar:" + profile.getAvatarUrl());
        return mapper.toDto(profile);
    }

    @Transactional
    @Override
    public ProfileResponseDTO update(MultipartFile avatarImage, ProfileUpdateDTO dto,  User user) {
        Profile profile = user.getProfile();

        if (dto.getCurrency() != null) {
            profile.setCurrency(dto.getCurrency());
        }

        if (dto.getCurrentBudget() != null) {
            profile.setCurrentBudget(dto.getCurrentBudget());
        }

        if (avatarImage != null) {
            if (profile.getAvatarUrl() != null) {
                storageService.delete("public", AVATAR_FOLDER, profile.getAvatarUrl());
            }

            String newFileName = storageService.save(
                    avatarImage,
                    "public",
                    AVATAR_FOLDER,
                    user.getId().toString(),
                    imageValidator
            );

            profile.setAvatarUrl(AVATAR_FOLDER + "/" + newFileName);
        }

        repo.save(profile);
        return mapper.toDto(profile);
    }
}