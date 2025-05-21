package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.RewardRuleDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.service.ChallengeRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for fetching reward rules (payout tiers) and computing payouts.
 *
 * Base path: /api/challenges/{challengeId}/rewards
 */
@RestController
@RequestMapping("/api/challenges/{challengeId}/rewards")
@RequiredArgsConstructor
public class ChallengeRewardController {
    private final ChallengeRewardService service;

    /**
     * GET  /api/challenges/{challengeId}/rewards/{role}
     *
     */
     @GetMapping
     public List<RewardRuleDto> listAll(
     @PathVariable Long challengeId
     ) {
     return service.listAllRewardRules(challengeId);
     }
     /** List all payout tiers for a given role in the challenge.
     *
     * @param challengeId  ID of the challenge
     * @param roleCategory path‐variable (AGENT, COMMERCIAL, etc.)
     */
    @GetMapping("/{role}")
    public List<RewardRuleDto> listRewardRules(
            @PathVariable Long challengeId,
            @PathVariable("role") Role roleCategory
    ) {
        return service.listRewardRules(challengeId, roleCategory);
    }

    /**
     * GET  /api/challenges/{challengeId}/rewards/{role}/compute
     * ?unitCount={unitCount}&metricValue={metricValue}
     *
     * Compute the actual reward for the given parameters.
     *
     * @param challengeId  ID of the challenge
     * @param roleCategory path‐variable (AGENT, COMMERCIAL, etc.)
     * @param unitCount    request‐param: the “unit” count (e.g. number of contracts)
     * @param metricValue  request‐param: the raw metric (e.g. total revenue or score)
     * @return the calculated reward amount
     */
    @GetMapping("/{role}/compute")
    public double computeReward(
            @PathVariable Long challengeId,
            @PathVariable("role") Role roleCategory,
            @RequestParam Long unitCount,
            @RequestParam double metricValue
    ) {
        return service.computeReward(challengeId, roleCategory, unitCount, metricValue);
    }
}
