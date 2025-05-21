// src/main/java/com/starboost/starboost_backend_demo/controller/admin/ChallengeAdminController.java
package com.starboost.starboost_backend_demo.controller.admin;

import com.starboost.starboost_backend_demo.dto.ChallengeDto;
import com.starboost.starboost_backend_demo.service.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/challenges")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ChallengeAdminController {
    private final ChallengeService challengeService;

    @GetMapping
    public List<ChallengeDto> listAll() {
        return challengeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(challengeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ChallengeDto> create(
            @Valid @RequestBody ChallengeDto dto
    ) {
        ChallengeDto created = challengeService.create(dto);
        return ResponseEntity.created(URI.create("/api/admin/challenges/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChallengeDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ChallengeDto dto
    ) {
        return ResponseEntity.ok(challengeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        challengeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
