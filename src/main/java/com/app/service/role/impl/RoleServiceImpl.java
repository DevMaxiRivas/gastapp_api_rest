package com.app.service.role.impl;

import com.app.model.Role;
import com.app.repository.RoleRepository;
import com.app.service.role.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repo;

    @Override
    public Optional<Role> findByName(String name) {
        return repo.findByName(name);
    }
}
