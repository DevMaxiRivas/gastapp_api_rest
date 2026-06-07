package com.app.mapper.auth;

import com.app.dto.v1.auth.claim.TokenClaimDTO;
import com.app.model.User;
import com.app.util.StorageUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = { StorageUtils.class})
public interface AuthMapper {
    @Mapping(target = "profile.avatarUrl", expression = "java(StorageUtils.generateURLPublic(profile.getAvatarUrl()))")
    @Mapping(target = "role", expression = "java(user.getRole().getName())")
    TokenClaimDTO toDto(User user);
}