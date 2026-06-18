package com.app.config.auth;

import com.app.middleware.auth.JwtAuthFilter;
import com.app.repository.UserRepository;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Security configuration for the application.
 *
 * <p>Defines the security filter chain, authentication manager, and password encoder.
 * Configures which endpoints are publicly accessible and which require authentication,
 * using a stateless JWT-based authentication strategy.</p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserRepository userRepository;
    private final AuthEntryPoint authEntryPoint;

    @Bean
    @Profile("dev")
    public CorsConfigurationSource corsConfigurationSource(@Value("${frontend.application.url}") String frontendUrl) {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> origins = List.of(frontendUrl);
        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all paths
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPoint))
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                // Permit multiple specific paths
                                .requestMatchers(
                                        "/",
                                        "/api/v1/auth/**",
                                        "/api/v1/greet/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v1/api-docs/**",
                                        "/storage/public/**"
                                )
                                .permitAll()

                                // Enable the sending of asynchronous events
                                .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll()
                                // Authenticate all other requests
                                .anyRequest()
                                .authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Provides the UserDetailsService implementation.
     * Explicitly defined to avoid Spring Security's autoconfiguration
     * from causing a circular dependency when building the AuthenticationManager.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: " + email));
    }

    /**
     * Configures the DaoAuthenticationProvider with the UserDetailsService
     * and PasswordEncoder, giving Spring Security an explicit and unambiguous
     * authentication provider and breaking the circular dependency.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Exposes the AuthenticationManager as a Spring bean.
     * Now resolves cleanly because the AuthenticationProvider
     * is explicitly defined above.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}