package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.WinnerDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.service.ChallengeEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Exposes the final “winners” list for a challenge:
 *
 *   GET  /api/challenges/{challengeId}/winners
 *   GET  /api/challenges/{challengeId}/winners/{role}
 */
@RestController
@RequestMapping("/api/challenges/{challengeId}")
@RequiredArgsConstructor
public class ChallengeEvaluationController {

    private final ChallengeEvaluationService evaluationService;

    /**
     * GET  /api/challenges/{challengeId}/winners
     * Returns every winner across all roles in this challenge.
     */
    @GetMapping("/winners")
    public List<WinnerDto> listWinners(@PathVariable Long challengeId) {
        return evaluationService.listWinners(challengeId);
    }

    /**
     * GET  /api/challenges/{challengeId}/winners/{role}
     * Returns winners only for the given role (AGENT, COMMERCIAL, etc.),
     * but filters out any participants with no reward (rewardAmount <= 0).
     */
    @GetMapping("/winners/{role}")
    public List<WinnerDto> listWinnersByRole(
            @PathVariable Long challengeId,
            @PathVariable Role role
    ) {
        // 1) fetch all winners for the role (including zero-reward)
        List<WinnerDto> initialList = evaluationService.listWinnersByRole(challengeId, role);

        // 2) filter out participants who did not earn any reward
        // Since rewardAmount is primitive double, just check > 0
        return initialList.stream()
                .filter(w -> w.getRewardAmount() > 0)
                .collect(Collectors.toList());
    }
}
