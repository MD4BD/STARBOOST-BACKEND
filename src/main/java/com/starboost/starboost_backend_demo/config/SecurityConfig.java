// src/main/java/com/starboost/starboost_backend_demo/config/SecurityConfig.java
package com.starboost.starboost_backend_demo.config;

import com.starboost.starboost_backend_demo.util.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Main security configuration for JWT-based auth, role-based URL protection,
 * and stateless session management.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // disable sessions, CSRF, form login, basic auth
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())

                // 2) Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // 1) Allow anonymous access to auth endpoints
                        .requestMatchers("/api/auth/**").permitAll()

                        // 2) Admin APIs (only ROLE_ADMIN can access)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 3) User-facing “/me” endpoints (any authenticated user)
                        .requestMatchers("/api/me/**").authenticated()

                        // 4) (Optional) URL-level role rules—if you still want them here
                        // .requestMatchers("/api/commercial/**").hasRole("COMMERCIAL")
                        // .requestMatchers("/api/agency-manager/**").hasRole("AGENCY_MANAGER")
                        // .requestMatchers("/api/regional-manager/**").hasRole("REGIONAL_MANAGER")
                        // .requestMatchers("/api/agent/**").hasRole("AGENT")
                        // .requestMatchers("/api/animator/**").hasRole("ANIMATOR")

                        // 5) All other /api/** endpoints require authentication
                        .requestMatchers("/api/**").authenticated()

                        // 6) Everything else (static, actuator, health checks, etc.) can be open
                        .anyRequest().permitAll()
                )

                // 3) JWT filter before the UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Expose the AuthenticationManager built by Spring so it can be injected
     * into AuthController for performing login authentication.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Expose a BCryptPasswordEncoder so Spring can auto-wire PasswordEncoder
     * into services and the authentication provider.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
