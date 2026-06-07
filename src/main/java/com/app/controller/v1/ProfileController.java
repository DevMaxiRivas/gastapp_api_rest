package com.app.controller.v1;

import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.profile.ProfileCreateDTO;
import com.app.dto.v1.profile.ProfileResponseDTO;
import com.app.dto.v1.profile.ProfileUpdateDTO;
import com.app.model.User;
import com.app.service.profile.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/me/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Operation(summary = "Create profile")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    encoding = {
                            @Encoding(name = "avatarImage", contentType = "image/jpeg, image/png, image/webp"),
                            @Encoding(name = "data",        contentType = "application/json")
                    }
            )
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> createProfile(
            @Valid @RequestPart("data") ProfileCreateDTO dto,
            @RequestPart(name="avatarImage", required = false) MultipartFile file,
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


    @Operation(summary = "Update profile")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    encoding = {
                            @Encoding(name = "avatarImage", contentType = "image/jpeg, image/png, image/webp"),
                            @Encoding(name = "data",        contentType = "application/json")
                    }
            )
    )
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> updateProfile(
            @Valid @RequestPart("data") ProfileUpdateDTO dto,
            @RequestPart(name="avatarImage", required = false) MultipartFile file,
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
