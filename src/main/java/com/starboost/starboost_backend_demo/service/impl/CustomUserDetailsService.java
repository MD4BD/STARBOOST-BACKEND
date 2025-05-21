package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.entity.User;
import com.starboost.starboost_backend_demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // 1) Look up the user (or 401 if not found)
        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No user with email: " + email)
                );

        // 2) Enforce the "active" flag
        if (!user.isActive()) {
            // Spring Security will catch this and treat it as a disabled account
            throw new DisabledException("Account is inactive");
        }

        // 3) Build the granted authority (prefixing ROLE_ is standard)
        GrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        // 4) Return Spring’s built-in UserDetails, marking everything valid
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authority)
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                // Since we already threw if inactive, this stays enabled
                .disabled(false)
                .build();
    }
}
