package com.app.middleware.auth;

import com.app.dto.v1.auth.Token;
import com.app.enums.token.TokenType;
import com.app.model.User;
import com.app.repository.UserRepository;
import com.app.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT authentication filter that intercepts every incoming HTTP request.
 *
 * <p>Extends {@link OncePerRequestFilter} to guarantee a single execution per request.
 * Extracts the JWT from the {@code Authorization} header, validates it against the
 * database, and sets the authentication in the {@link SecurityContextHolder} when
 * the token is valid.</p>
 *
 * <p>Token validation involves two checks:</p>
 * <ul>
 *   <li><b>Signature and expiration:</b> verified by {@link JwtService}.</li>
 *   <li><b>Active session:</b> the token must be present in the user's {@code tokens[]}
 *       column. This allows immediate invalidation on logout, regardless of
 *       the token's expiration time.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    /**
     * Intercepts each request, extracts and validates the JWT, and registers
     * the authenticated user in the {@link SecurityContextHolder}.
     *
     * <p>The filter is skipped when:</p>
     * <ul>
     *   <li>The {@code Authorization} header is absent.</li>
     *   <li>The header does not start with {@code "Bearer "}.</li>
     * </ul>
     *
     * <p>Authentication is set only when all of the following conditions are met:</p>
     * <ol>
     *   <li>The token yields a valid email.</li>
     *   <li>No authentication is already present in the security context.</li>
     *   <li>The user exists in the database.</li>
     *   <li>The token is found in the user's active {@code tokens[]} array.</li>
     *   <li>The token passes signature and expiration validation.</li>
     * </ol>
     *
     * @param request  the incoming {@link HttpServletRequest}
     * @param response the outgoing {@link HttpServletResponse}
     * @param chain    the {@link FilterChain} to continue processing
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException,
            IOException
    {
        // System.out.println(request.getRequestURL().toString());
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        Token token = new Token(
                authHeader.substring(7),
                TokenType.ACCESS_TOKEN
        );
        String email = jwtService.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null && jwtService.isTokenValid(token, user)) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                user, null,
                                user.getAuthorities()
                        );
                auth.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}