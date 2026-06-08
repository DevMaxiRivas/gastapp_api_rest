package com.app.db.seeder.role;

import com.app.enums.permissions.PermissionEnum;
import com.app.enums.permissions.ResourceEnum;
import com.app.model.Permission;
import com.app.model.Role;
import com.app.repository.PermissionRepository;
import com.app.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RolesInitializer {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RolesInitializer(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @PostConstruct
    public void seedRolesAndPermissions() {
        // Avoid duplicate seeding
        if (roleRepository.count() > 0) return;

        // Save permissions
        Permission createUser = permissionRepository.save(new Permission(null, ResourceEnum.USERS, PermissionEnum.CREATE));
        Permission readAllUsers = permissionRepository.save(new Permission(null, ResourceEnum.USERS, PermissionEnum.READ_ALL));
        Permission readProfile = permissionRepository.save(new Permission(null, ResourceEnum.PROFILES, PermissionEnum.READ));

        Role admin = new Role();
        admin.setName("ADMIN");
        admin.setPermissions(Set.of(readAllUsers, createUser));
        roleRepository.save(admin);

        Role moderator = new Role();
        moderator.setName("MODERATOR");
        moderator.setPermissions(Set.of(readAllUsers));
        roleRepository.save(moderator);

        Role user = new Role();
        user.setName("USER");
        user.setPermissions(Set.of(readProfile));
        roleRepository.save(user);
    }

}