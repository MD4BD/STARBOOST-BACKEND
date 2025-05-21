// src/main/java/com/starboost/starboost_backend_demo/controller/ChallengeController.java
package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.ChallengeDto;
import com.starboost.starboost_backend_demo.dto.RuleDto;
import com.starboost.starboost_backend_demo.service.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping
    public ResponseEntity<List<ChallengeDto>> getAll() {
        return ResponseEntity.ok(challengeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(challengeService.findById(id));
    }

    /** Create a new Challenge */
    @PostMapping
    public ResponseEntity<ChallengeDto> create(
            @Valid @RequestBody ChallengeDto dto
    ) {
        ChallengeDto created = challengeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
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

    /** Score‐rules endpoint  */
    @GetMapping("/{id}/rules")
    public ResponseEntity<Map<String, List<RuleDto>>> getRules(@PathVariable Long id) {
        List<RuleDto> rules = challengeService.findById(id).getRules();
        return ResponseEntity.ok(Collections.singletonMap("rules", rules));
    }
}
