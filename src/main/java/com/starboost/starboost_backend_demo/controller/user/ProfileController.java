// src/main/java/com/starboost/starboost_backend_demo/controller/user/ProfileController.java
package com.starboost.starboost_backend_demo.controller.user;

import com.starboost.starboost_backend_demo.dto.UserDto;
import com.starboost.starboost_backend_demo.dto.user.ProfileUpdateDto;
import com.starboost.starboost_backend_demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    /** GET /api/me */
    @GetMapping
    public ResponseEntity<UserDto> getProfile(
            @AuthenticationPrincipal UserDetails principal
    ) {
        UserDto me = userService.findByEmail(principal.getUsername());
        return ResponseEntity.ok(me);
    }

    /** PUT /api/me */
    @PutMapping
    public ResponseEntity<UserDto> updateProfile(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody ProfileUpdateDto dto
    ) {
        UserDto me = userService.findByEmail(principal.getUsername());
        UserDto updated = userService.updateProfile(me.getId(), dto);
        return ResponseEntity.ok(updated);
    }
}
