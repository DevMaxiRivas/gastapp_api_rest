package com.app.service.security;

import com.app.model.User;
import com.app.repository.CategoryRepository;
import com.app.repository.TransactionRepository;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("securityService")
@RequiredArgsConstructor
public class SecurityService {
    /*
    * The repositories are injected directly because the services could cause circular dependency issues.
    * */
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public boolean canAccessTransaction(Long transactionId) {
        if (isAdmin()) return true;
        return transactionRepository.existsByIdAndUserId(transactionId, getAuthenticatedUser().getId());
    }

    public boolean canAccessCategory(Long categoryId) {
        if (isAdmin()) return true;
        return categoryRepository.existsByIdAndUserId(categoryId, getAuthenticatedUser().getId());
    }

    private boolean isAdmin() {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        if(user == null) throw new IllegalStateException("Authentication object is null");

        return user
                .getAuthorities()
                .stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));
    }

    private User getAuthenticatedUser() {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        if(user == null) throw new IllegalStateException("Authentication object is null");
        return (User) user.getPrincipal();
    }
}