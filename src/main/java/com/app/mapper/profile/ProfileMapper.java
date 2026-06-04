package com.app.mapper.profile;

import com.app.dto.v1.profile.ProfileResponseDTO;
import com.app.model.Profile;
import com.app.util.StorageUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {StorageUtils.class})
public interface ProfileMapper {
    @Mapping(target = "avatarUrl", expression = "java(StorageUtils.generateURLPublic(profile.getAvatarUrl()))")
    ProfileResponseDTO toDto(Profile profile);

    Profile toEntity(ProfileResponseDTO dto);
}
