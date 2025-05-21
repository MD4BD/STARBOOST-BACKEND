// src/main/java/com/starboost/starboost_backend_demo/util/JwtUtil.java

package com.starboost.starboost_backend_demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for generating and validating JWT tokens.
 */
@Component
public class JwtUtil {

    // Secret key from application.properties (32 ASCII chars → 256-bit)
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Token expiration in milliseconds (e.g. 86400000 = 24h)
    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    // Signing key built once after bean initialization
    private Key signingKey;

    @PostConstruct
    public void init() {
        // Convert ASCII secret to HMAC-SHA256 key
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generate an HS256-signed JWT for the authenticated user.
     * @param auth Spring Security Authentication containing user details
     * @return compact JWT string
     */
    public String generateToken(org.springframework.security.core.Authentication auth) {
        var userDetails = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        // Collect role names as strings
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(granted -> granted.getAuthority())
                .collect(Collectors.toList());

        Date now    = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())    // 'sub' claim = email
                .claim("roles", roles)                    // custom 'roles' claim
                .setIssuedAt(now)                         // 'iat' claim
                .setExpiration(expiry)                    // 'exp' claim
                .signWith(signingKey)                     // sign with HS256 key
                .compact();
    }

    /**
     * Extract subject (username/email) from JWT.
     */
    public String getUsernameFromToken(String token) {
        return parse(token).getBody().getSubject();
    }

    /**
     * Extract list of roles from JWT 'roles' claim.
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        return (List<String>) parse(token).getBody().get("roles");
    }

    /**
     * Validate token signature and expiration.
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false; // invalid token
        }
    }

    /**
     * Internal parser helper building parser with signing key.
     */
    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
    }
}
