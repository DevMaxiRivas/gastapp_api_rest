package com.app.service.role;

import com.app.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
}
