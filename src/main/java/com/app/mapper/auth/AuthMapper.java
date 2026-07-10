package com.app.mapper.auth;

import com.app.dto.v1.auth.claim.TokenClaimDTO;
import com.app.mapper.config.GlobalMapperConfig;
import com.app.mapper.helper.RoleMapper;
import com.app.mapper.helper.StorageMapper;
import com.app.mapper.helper.StringMapper;
import com.app.mapper.qualifier.storage.GetPublicPath;
import com.app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    config = GlobalMapperConfig.class,
    uses = {
        StringMapper.class,
        RoleMapper.class,
        StorageMapper.class
    }
)
public interface AuthMapper {
    @Mapping(
        target = "profile.avatarUrl",
        source = "profile.avatarUrl",
        qualifiedBy = GetPublicPath.class
    )
    @Mapping(
        target = "role",
        source = "user"
    )
    @Mapping(
        target = "username",
        expression = "java(user.getUsernameApp())"
    )
    TokenClaimDTO toDto(User user);
}