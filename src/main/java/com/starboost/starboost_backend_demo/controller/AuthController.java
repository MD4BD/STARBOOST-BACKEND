// src/main/java/com/starboost/starboost_backend_demo/controller/AuthController.java
package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.LoginRequest;
import com.starboost.starboost_backend_demo.dto.LoginResponse;
import com.starboost.starboost_backend_demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controller handling authentication-related endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // AuthenticationManager is provided by Spring Security to perform authentication
    private final AuthenticationManager authenticationManager;

    // JwtUtil generates and validates JWT tokens
    private final JwtUtil jwtUtil;

    /**
     * Log in with email and password.
     *
     * @param req Contains email and password supplied by the user.
     * @return A JWT wrapped in a LoginResponse on successful authentication.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest req) {

        // 1. Build an authentication token using the provided email and password
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),    // Principal: email address
                        req.getPassword()  // Credentials: raw password
                );

        // 2. Authenticate the token (throws exception if invalid)
        Authentication auth = authenticationManager.authenticate(authToken);

        // 3. Store the authentication result in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 4. Generate a JWT for the authenticated user
        String jwt = jwtUtil.generateToken(auth);

        // 5. Return the JWT in a standardized response DTO
        return ResponseEntity.ok(new LoginResponse(jwt));
    }
}
