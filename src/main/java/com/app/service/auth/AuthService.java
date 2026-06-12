package com.app.service.auth;

import com.app.dto.v1.auth.*;
import com.app.dto.v1.auth.claim.TokenClaimDTO;
import com.app.enums.token.TokenTypeEnum;
import com.app.exception.app.auth.jwt.InvalidJwtCustomException;
import com.app.exception.body.ValidationRequestBodyCustomException;
import com.app.mapper.auth.AuthMapper;
import com.app.model.User;
import com.app.service.user.UserService;
import com.app.util.HashingUtils;
import com.app.util.MapUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final JwtService jwtService;
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
        User user = userService.findByEmail(email)
                .orElseThrow(InvalidJwtCustomException::new);

        String hashToken = HashingUtils.hashToken(refreshToken);
        userService.removeRefreshToken(user, hashToken);
    }

    public AuthResponse register(RegisterRequest request) {
        User user = userService.create(request);
        AuthResponse authResponse = generateTokens(user);
        String hashToken = HashingUtils.hashToken(authResponse.refreshToken());
        userService.addRefreshToken(user, hashToken);

        return authResponse;
    }

    public AuthResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email().toLowerCase(),
                        request.password()
                )
        );

        User user = userService.findByEmail(request.email())
                .orElseThrow(() -> new ValidationRequestBodyCustomException("User not found", "body.email"));

        AuthResponse authResponse = generateTokens(user);
        String hashToken = HashingUtils.hashToken(authResponse.refreshToken());
        userService.addRefreshToken(user, hashToken);
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
            }
            throw e;
        }
    }

    @Transactional(dontRollbackOn = ExpiredJwtException.class)
    public AccessTokenResponse refresh(String refreshToken) {
        try {
            String email = jwtService.extractEmail(new Token(refreshToken, TokenTypeEnum.REFRESH_TOKEN));
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new ValidationRequestBodyCustomException("User not found", "token.email"));

            TokenClaimDTO data = authMapper.toDto(user);
            Map<String, Object> map = MapUtils.convertToMap(data);
            String accessToken = jwtService.generateToken(user, map, TokenTypeEnum.ACCESS_TOKEN);
            return new AccessTokenResponse(accessToken);
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                String email = ((ExpiredJwtException) e).getClaims().getSubject();
                removeRefreshToken(email, refreshToken);
            }
            throw e;
        }
    }
}