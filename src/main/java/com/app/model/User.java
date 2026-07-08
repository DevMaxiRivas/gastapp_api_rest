package com.app.model;

import jakarta.persistence.*;
import lombok.*;

import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "text[]")
    private List<String> tokens;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Profile profile;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    public String getUsernameApp() { return username; }

    // --- UserDetails for Auth---
    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + this.getRole().getName()));
            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        return authorities;
    }

    @Override
    @NullMarked
    public String getUsername() { return email; }

    @Override
    @NullMarked
    public boolean isAccountNonExpired()  { return true; }

    @Override
    @NullMarked
    public boolean isAccountNonLocked()   { return true; }

    @Override
    @NullMarked
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    @NullMarked
    public boolean isEnabled()            { return true; }
}