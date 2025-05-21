// src/main/java/com/starboost/starboost_backend_demo/controller/admin/UserAdminController.java
package com.starboost.starboost_backend_demo.controller.admin;

import com.starboost.starboost_backend_demo.dto.UserDto;
import com.starboost.starboost_backend_demo.dto.admin.UserCreateDto;
import com.starboost.starboost_backend_demo.dto.admin.UserUpdateDto;
import com.starboost.starboost_backend_demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> listAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> create(
            @Valid @RequestBody UserCreateDto dto
    ) {
        UserDto created = userService.create(dto.toUserDto());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto dto
    ) {
        UserDto updated = userService.update(id, dto.toUserDto());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
