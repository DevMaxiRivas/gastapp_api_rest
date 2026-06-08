package com.app.service.auth;

import com.app.dto.v1.auth.Token;
import com.app.enums.token.TokenTypeEnum;
import com.app.model.Permission;
import com.app.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${spring.application.jwt_secret}")
    private String secretKey;

    @Value("${spring.application.jwt_secret_expiration}")
    private long expiration;

    @Value("${spring.application.jwt_refresh_secret}")
    private String refreshSecretKey;

    @Value("${spring.application.jwt_refresh_secret_expiration}")
    private long refreshExpiration;

    private SecretKey getSigningKey(TokenTypeEnum tokenType) {
        if (tokenType == TokenTypeEnum.ACCESS_TOKEN) {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        }

        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
    }

    private long getDurationToken(TokenTypeEnum tokenType){
        if (tokenType == TokenTypeEnum.ACCESS_TOKEN) return expiration;
        return  refreshExpiration;
    }


    public String generateToken(User user,
                                Map<String, Object> data,
                                TokenTypeEnum tokenType) {
        String role = user.getRole().getName();
        Set<String> permissions = user.getRole().getPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .subject(user.getEmail())
                .claims(data)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + getDurationToken(tokenType)))
                .signWith(getSigningKey(tokenType))
                .compact();
    }

    public String extractEmail(Token token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(Token token, UserDetails user) {
        return extractEmail(token).equals(user.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(Token token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(Token token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey(token.getTokenType()))
                .build()
                .parseSignedClaims(token.getValue())
                .getPayload();
        return resolver.apply(claims);
    }
}