package com.app.mapper.profile;

import com.app.dto.v1.profile.ProfileCreateDTO;
import com.app.dto.v1.profile.ProfileResponseDTO;
import com.app.mapper.config.GlobalMapperConfig;
import com.app.mapper.helper.StorageMapper;
import com.app.mapper.qualifier.storage.GetPublicPath;
import com.app.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    config = GlobalMapperConfig.class,
    uses = StorageMapper.class
)
public interface ProfileMapper {
    @Mapping(
        target = "avatarUrl",
        source = "avatarUrl",
        qualifiedBy = GetPublicPath.class
    )
    ProfileResponseDTO toDto(Profile profile);

    @Mapping(target = "user_id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    Profile toEntity(ProfileCreateDTO dto);
}
