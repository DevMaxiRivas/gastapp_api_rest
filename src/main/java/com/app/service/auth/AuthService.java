package com.app.service.auth;

import com.app.dto.v1.auth.*;
import com.app.dto.v1.auth.claim.TokenClaimDTO;
import com.app.enums.token.TokenTypeEnum;
import com.app.exception.GenericErrorException;
import com.app.exception.app.auth.jwt.InvalidJwtCustomException;
import com.app.exception.body.ValidationRequestBodyCustomException;
import com.app.mapper.auth.AuthMapper;
import com.app.model.Role;
import com.app.model.User;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.util.HashingUtils;
import com.app.util.MapUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    private final AuthMapper  authMapper;

    protected AuthResponse generateTokens(User user) {
        TokenClaimDTO data = authMapper.toDto(user);
            Map<String, Object> map = MapUtils.convertToMap(data);
            String token = jwtService.generateToken(user, map, TokenTypeEnum.ACCESS_TOKEN);
            String refreshToken = jwtService.generateToken(user, map, TokenTypeEnum.REFRESH_TOKEN);
            return new AuthResponse(new AccessTokenResponse(token), refreshToken);
    }

    protected void removeRefreshToken(String email, String refreshToken) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(InvalidJwtCustomException::new);

        String hashToken = HashingUtils.hashToken(refreshToken);
        userRepo.removeToken(user.getId(), hashToken);
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepo.findByEmail(request.email()).isPresent()) {
            throw new ValidationRequestBodyCustomException("email: is already in use", "body.email" );
        }

        Role defaultRole = roleRepo.findByName("USER").orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(defaultRole)
                .tokens(List.of())
                .build();

        userRepo.save(user);

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

    @Transactional(dontRollbackOn = ExpiredJwtException.class)
    public void logout(String refreshToken) {
        try {
            String email = jwtService.extractEmail(new Token(refreshToken, TokenTypeEnum.REFRESH_TOKEN));
            removeRefreshToken(email, refreshToken);
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                String email = ((ExpiredJwtException) e).getClaims().getSubject();
                removeRefreshToken(email, refreshToken);
                throw e;
            } else {
                throw new GenericErrorException(e);
            }
        }
    }

    @Transactional(dontRollbackOn = ExpiredJwtException.class)
    public AccessTokenResponse refresh(String refreshToken) {
        try {
            String email = jwtService.extractEmail(new Token(refreshToken, TokenTypeEnum.REFRESH_TOKEN));
            User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new ValidationRequestBodyCustomException("User not found", "token.email"));

            TokenClaimDTO data = authMapper.toDto(user);
            Map<String, Object> map = MapUtils.convertToMap(data);
            String token = jwtService.generateToken(user, map, TokenTypeEnum.ACCESS_TOKEN);
            return new AccessTokenResponse(token);
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                String email = ((ExpiredJwtException) e).getClaims().getSubject();
                removeRefreshToken(email, refreshToken);
                throw e;
            } else {
                throw new GenericErrorException(e);
            }
        }
    }
}