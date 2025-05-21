package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.RewardRuleDto;
import com.starboost.starboost_backend_demo.entity.Role;
import java.util.List;

/**
 * Service for listing payout tiers (reward rules) and computing actual rewards.
 */
public interface ChallengeRewardService {

    /**
     * Fetch all reward‐rule definitions (tiers & base amounts) for a given challenge and role.
     *
     * @param challengeId  ID of the challenge
     * @param roleCategory the role (e.g. AGENT, COMMERCIAL, etc.)
     * @return a list of RewardRuleDto, each representing one payout tier
     */
    List<RewardRuleDto> listRewardRules(Long challengeId, Role roleCategory);

    /**
     * Compute the reward amount for a given challenge & role, based on:
     *   - unitCount:   number of “units” (e.g. contracts or sales points)
     *   - metricValue: total metric (e.g. revenue or score) to apply against the tier thresholds
     *
     * Finds the first tier where
     *    metricValue ≥ tierMin * unitCount
     * AND
     *    metricValue ≤ tierMax * unitCount
     * and returns that tier’s baseAmount. Returns 0.0 if no tier matches.
     *
     * @param challengeId  ID of the challenge
     * @param roleCategory the role
     * @param unitCount    the “count” to multiply against tier thresholds
     * @param metricValue  the actual metric (revenue, score…)
     * @return the calculated reward, or 0.0 if no matching tier
     */
    double computeReward(Long challengeId,
                         Role roleCategory,
                         Long unitCount,
                         double metricValue);

    List<RewardRuleDto> listAllRewardRules(Long challengeId);

}
