package com.app.service.auth;

import com.app.dto.v1.auth.*;
import com.app.enums.user.CurrencyType;
import com.app.exception.ValidationRequestBodyException;
import com.app.model.User;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepo.findByEmail(request.email()).isPresent()) {
            throw new ValidationRequestBodyException("email: is already in use", "body/email" );
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .currency(request.currency())
                .tokens(List.of())
                .build();

        String token = jwtService.generateToken(user);
        user.setTokens(List.of(token));
        userRepo.save(user);

        return new AuthResponse(token, user.getEmail(), user.getName());
    }

    public AuthResponse login(LoginRequest request) {
        System.out.println("Check Credentials");
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        System.out.println("Login Success");

        User user = userRepo.findByEmail(request.email())
                .orElseThrow(() -> new ValidationRequestBodyException("User not found", "body/email"));

        String token = jwtService.generateToken(user);

        List<String> currentTokens = user.getTokens() != null ? user.getTokens() : new ArrayList<String>();
        currentTokens.add(token);
        user.setTokens(currentTokens);
        userRepo.save(user);

        return new AuthResponse(token, user.getEmail(), user.getName());
    }

    public void logout(String token) {
        String email = jwtService.extractEmail(token);
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ValidationRequestBodyException("User not found", "token/email"));

        List<String> updatedTokens = new ArrayList<>(user.getTokens());
        updatedTokens.remove(token);

        user.setTokens(updatedTokens);
        userRepo.save(user);
    }
}