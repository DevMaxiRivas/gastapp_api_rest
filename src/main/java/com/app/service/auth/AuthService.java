package com.app.service.auth;

import com.app.dto.v1.auth.*;
import com.app.enums.token.TokenType;
import com.app.exception.body.ValidationRequestBodyCustomException;
import com.app.model.User;
import com.app.repository.UserRepository;
import com.app.util.HashingUtils;
import jakarta.transaction.Transactional;
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

    protected AuthResponse generateTokens(User user) {
        String token = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);
        return new AuthResponse(new AccessTokenResponse(token), refreshToken);
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepo.findByEmail(request.email()).isPresent()) {
            throw new ValidationRequestBodyCustomException("email: is already in use", "body.email" );
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .currency(request.currency())
                .tokens(List.of())
                .build();

        AuthResponse authResponse = generateTokens(user);
        user.setTokens(List.of(
                HashingUtils.hashToken(authResponse.refreshToken())
        ));
        userRepo.save(user);

        return authResponse;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepo.findByEmail(request.email())
                .orElseThrow(() -> new ValidationRequestBodyCustomException("User not found", "body.email"));

        AuthResponse authResponse = generateTokens(user);

        List<String> currentTokens = user.getTokens() != null ? user.getTokens() : new ArrayList<String>();
        currentTokens.add(
                HashingUtils.hashToken(authResponse.refreshToken())
        );

        user.setTokens(currentTokens);
        userRepo.save(user);
        return authResponse;
    }

    @Transactional
    public void logout(String refreshToken) {
        String email = jwtService.extractEmail(new Token(refreshToken, TokenType.REFRESH_TOKEN));
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ValidationRequestBodyCustomException("User not found", "token.email"));

        String hashToken = HashingUtils.hashToken(refreshToken);
        userRepo.removeToken(user.getId(), hashToken);
    }

    @Transactional
    public AccessTokenResponse refresh(String refreshToken) {
        String email = jwtService.extractEmail(new Token(refreshToken, TokenType.REFRESH_TOKEN));
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ValidationRequestBodyCustomException("User not found", "token.email"));

        String token = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        return new AccessTokenResponse(token);
    }
}