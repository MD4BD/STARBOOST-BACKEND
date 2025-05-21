package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.ScoreRuleDto;
import com.starboost.starboost_backend_demo.service.ScoreRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges/{challengeId}/score-rules")
@RequiredArgsConstructor
public class ScoreRuleController {
    private final ScoreRuleService scoreRuleService;

    @GetMapping
    public ResponseEntity<List<ScoreRuleDto>> getAll(@PathVariable Long challengeId) {
        return ResponseEntity.ok(
                scoreRuleService.findAllByChallengeId(challengeId));
    }

    @GetMapping("/{ruleId}")
    public ResponseEntity<ScoreRuleDto> getOne(@PathVariable Long challengeId,
                                               @PathVariable Long ruleId) {
        return ResponseEntity.ok(
                scoreRuleService.findById(challengeId, ruleId));
    }

    @PostMapping
    public ResponseEntity<ScoreRuleDto> create(@PathVariable Long challengeId,
                                               @RequestBody ScoreRuleDto dto) {
        ScoreRuleDto created =
                scoreRuleService.createForChallenge(challengeId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{ruleId}")
    public ResponseEntity<ScoreRuleDto> update(@PathVariable Long challengeId,
                                               @PathVariable Long ruleId,
                                               @RequestBody ScoreRuleDto dto) {
        return ResponseEntity.ok(
                scoreRuleService.updateForChallenge(challengeId, ruleId, dto));
    }

    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> delete(@PathVariable Long challengeId,
                                       @PathVariable Long ruleId) {
        scoreRuleService.deleteForChallenge(challengeId, ruleId);
        return ResponseEntity.noContent().build();
    }
}