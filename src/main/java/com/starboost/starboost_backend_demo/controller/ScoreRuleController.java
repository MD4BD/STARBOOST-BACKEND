// src/main/java/com/starboost/starboost_backend_demo/controller/ScoreRuleController.java
package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.ScoreRuleDto;
import com.starboost.starboost_backend_demo.service.ScoreRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin‐only controller for per-challenge scoring rules.
 * Exposes full CRUD under /api/admin/challenges/{challengeId}/score-rules.
 */
@RestController
// ← changed from "/api/challenges/…" to include the "admin" prefix
@RequestMapping("/api/admin/challenges/{challengeId}/score-rules")
@RequiredArgsConstructor
public class ScoreRuleController {
    private final ScoreRuleService scoreRuleService;

    /**
     * List all scoring rules for a given challenge.
     */
    @GetMapping
    public ResponseEntity<List<ScoreRuleDto>> getAll(@PathVariable Long challengeId) {
        return ResponseEntity.ok(scoreRuleService.findAllByChallengeId(challengeId));
    }

    /**
     * Fetch one scoring rule by its ID (scoped to the challenge).
     */
    @GetMapping("/{ruleId}")
    public ResponseEntity<ScoreRuleDto> getOne(@PathVariable Long challengeId,
                                               @PathVariable Long ruleId) {
        return ResponseEntity.ok(scoreRuleService.findById(challengeId, ruleId));
    }

    /**
     * Create a new scoring rule for this challenge.
     */
    @PostMapping
    public ResponseEntity<ScoreRuleDto> create(
            @PathVariable Long challengeId,
            @Valid @RequestBody ScoreRuleDto dto) {
        ScoreRuleDto created = scoreRuleService.createForChallenge(challengeId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing scoring rule for this challenge.
     */
    @PutMapping("/{ruleId}")
    public ResponseEntity<ScoreRuleDto> update(
            @PathVariable Long challengeId,
            @PathVariable Long ruleId,
            @Valid @RequestBody ScoreRuleDto dto) {
        return ResponseEntity.ok(
                scoreRuleService.updateForChallenge(challengeId, ruleId, dto));
    }

    /**
     * Delete a scoring rule by ID (scoped to the challenge).
     */
    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> delete(@PathVariable Long challengeId,
                                       @PathVariable Long ruleId) {
        scoreRuleService.deleteForChallenge(challengeId, ruleId);
        return ResponseEntity.noContent().build();
    }
}
