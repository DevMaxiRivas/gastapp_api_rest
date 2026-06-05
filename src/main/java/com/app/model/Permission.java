package com.app.model;

import com.app.enums.permissions.PermissionEnum;
import com.app.enums.permissions.ResourceEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "permissions",
        uniqueConstraints = @UniqueConstraint(name = "uq_permissions_resource_type", columnNames = {"resource", "type"})
)
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private ResourceEnum resource;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private PermissionEnum type;

    public String getName(){
        return this.resource + "_" + this.type;
    }
}