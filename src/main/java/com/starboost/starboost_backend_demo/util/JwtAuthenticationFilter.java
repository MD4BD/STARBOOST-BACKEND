// src/main/java/com/starboost/starboost_backend_demo/util/JwtAuthenticationFilter.java

package com.starboost.starboost_backend_demo.util;

import com.starboost.starboost_backend_demo.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that intercepts HTTP requests to validate JWT tokens.
 *
 * We skip any request whose path begins with "/api/auth" so that the
 * login endpoint (/api/auth/login) is not blocked by JWT-check.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        // 1) If this request is under /api/auth/**, skip JWT validation entirely
        String path = request.getServletPath();
        if (path.startsWith("/api/auth")) {
            chain.doFilter(request, response);
            return;
        }

        // 2) Otherwise, attempt to pull a Bearer token from the Authorization header
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String username = jwtUtil.getUsernameFromToken(token);

            // 3) If we got a non-null username and no Authentication yet, validate token
            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null
                    && jwtUtil.validateToken(token)) {

                var userDetails = userDetailsService.loadUserByUsername(username);
                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                auth.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // 4) Continue with the filter chain in all other cases
        chain.doFilter(request, response);
    }
}
