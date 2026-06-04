package com.app.controller.v1;

import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.profile.ProfileCreateDTO;
import com.app.dto.v1.profile.ProfileResponseDTO;
import com.app.dto.v1.profile.ProfileUpdateDTO;
import com.app.model.User;
import com.app.service.profile.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.attribute.UserPrincipal;

@RestController
@RequestMapping("/api/v1/me/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> createProfile(
            @Valid @RequestPart("data") ProfileCreateDTO dto,
            @RequestPart("avatarImage") MultipartFile file,
            @AuthenticationPrincipal User user
    ) {
        return
                ResponseEntity
                        .ok(
                                ApiResponse.success(
                                        profileService.create(file, dto, user)
                                )
                        );
    }


    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> updateProfile(
            @Valid @RequestPart("data") ProfileUpdateDTO dto,
            @RequestPart("avatarImage") MultipartFile file,
            @AuthenticationPrincipal User user
    ) {
        return
                ResponseEntity
                        .ok(
                                ApiResponse.success(
                                    profileService.update(file, dto, user)
                                )
                        );
    }
}
