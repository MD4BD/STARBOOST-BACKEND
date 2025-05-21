package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.WinnerDto;
import com.starboost.starboost_backend_demo.entity.ChallengeWinningRule;
import com.starboost.starboost_backend_demo.entity.ConditionType;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.repository.ChallengeWinningRuleRepository;
import com.starboost.starboost_backend_demo.service.ChallengeEvaluationService;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import com.starboost.starboost_backend_demo.service.ChallengePerformanceService;
import com.starboost.starboost_backend_demo.service.ChallengeRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Evaluates a Challenge end-to-end:
 * 1) loads participants by role,
 * 2) applies every eligibility gate (winning-rule),
 * 3) ranks survivors by the correct metric,
 * 4) computes each winner’s payout (reward-rule),
 * 5) returns a full list of WinnerDto.
 */
@Service
@RequiredArgsConstructor
public class ChallengeEvaluationServiceImpl implements ChallengeEvaluationService {
    private final ChallengeWinningRuleRepository winningRuleRepo;
    private final ChallengeRewardService         rewardService;
    private final ChallengeParticipantService    participantService;
    private final ChallengePerformanceService    performanceService;

    /**
     * Evaluate all roles in one go.
     */
    @Override
    public List<WinnerDto> listWinners(Long challengeId) {
        return Stream.of(Role.values())
                .flatMap(role -> listWinnersByRole(challengeId, role).stream())
                .collect(Collectors.toList());
    }

    /**
     * Evaluate winners for a single role (AGENT, COMMERCIAL, etc.).
     */
    @Override
    public List<WinnerDto> listWinnersByRole(Long challengeId, Role role) {
        // 1) fetch eligibility gates for this challenge+role
        List<ChallengeWinningRule> winRules =
                winningRuleRepo.findAllByChallengeIdAndRoleCategory(challengeId, role);

        // 2) list every userId enrolled in that role
        List<Long> userIds =
                participantService.listParticipantIds(challengeId, role);

        // 3) assemble each “candidate” (metrics + gate tests)
        List<Candidate> candidates = userIds.stream()
                .map(uid -> assembleCandidate(challengeId, uid, winRules))
                .filter(Objects::nonNull)   // null ⇒ failed at least one gate
                .collect(Collectors.toList());

        // 4) sort by the appropriate metric
        candidates.sort(getComparator(winRules).reversed());

        // 5) build & return your WinnerDto list
        List<WinnerDto> winners = new ArrayList<>();
        for (int i = 0; i < candidates.size(); i++) {
            Candidate c = candidates.get(i);
            int rank = i + 1;

            // for rank-based payouts: unitCount=1, metricValue=rank
            double payout = rewardService.computeReward(
                    challengeId, role, 1L, rank
            );

            winners.add(WinnerDto.builder()
                    .userId(c.userId)
                    .roleCategory(role)
                    .agencyId(c.agencyId)
                    .regionId(c.regionId)
                    .rank(rank)
                    .contractCount(c.contractCount)
                    .revenue(c.revenue)
                    .score(c.score)
                    .average(c.average)
                    .weightedAverage(c.weightedAverage)
                    .rewardAmount(payout)
                    .build());
        }

        return winners;
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────────

    /**
     * Gather all raw metrics for one user, then test every gate.
     * Returns null if they fail any; otherwise returns a full Candidate.
     */
    private Candidate assembleCandidate(
            Long challengeId,
            Long userId,
            List<ChallengeWinningRule> winRules
    ) {
        Candidate c = new Candidate();
        c.userId        = userId;
        c.agencyId      = participantService.getAgencyIdForUser(userId);
        c.regionId      = participantService.getRegionIdForUser(userId);
        c.contractCount = performanceService.getContractCount(challengeId, userId);
        c.revenue       = performanceService.getRevenue(challengeId, userId);
        c.score         = performanceService.getScore(challengeId, userId);

        // flat‐average (agency or region)
        if (winRules.stream().anyMatch(r -> r.getConditionType() == ConditionType.MIN_AVG_PER_COMMERCIAL)) {
            double totRev    = performanceService.getAgencyTotalRevenue(challengeId, c.agencyId);
            long   commCount = participantService.countByAgency(challengeId, c.agencyId);
            c.average = commCount > 0 ? totRev / commCount : null;
        }
        if (winRules.stream().anyMatch(r -> r.getConditionType() == ConditionType.MIN_AVG_PER_PV)) {
            double totRev  = performanceService.getRegionTotalRevenue(challengeId, c.regionId);
            long   pvCount = performanceService.getRegionPVCount(challengeId, c.regionId);
            c.average = pvCount > 0 ? totRev / pvCount : c.average;
        }

        // weighted‐average (agency or region)
        if (winRules.stream().anyMatch(r -> r.getConditionType() == ConditionType.WEIGHTED_AVG_AGENCY)) {
            int    totScore = performanceService.getAgencyTotalScore(challengeId, c.agencyId);
            long   commCount = participantService.countByAgency(challengeId, c.agencyId);
            double weight    = computeAgencyWeight(commCount);
            c.weightedAverage = commCount > 0
                    ? (totScore / (double) commCount) * weight
                    : null;
        }
        if (winRules.stream().anyMatch(r -> r.getConditionType() == ConditionType.WEIGHTED_AVG_REGION)) {
            int    totScore = performanceService.getRegionTotalScore(challengeId, c.regionId);
            long   pvCount  = performanceService.getRegionPVCount(challengeId, c.regionId);
            double weight   = computeRegionWeight(pvCount);
            c.weightedAverage = pvCount > 0
                    ? (totScore / (double) pvCount) * weight
                    : c.weightedAverage;
        }

        // final gate: must pass all rules
        return passesAllWinningRules(c, winRules) ? c : null;
    }

    /** Returns true if candidate meets every single eligibility rule. */
    private boolean passesAllWinningRules(Candidate c, List<ChallengeWinningRule> rules) {
        for (var r : rules) {
            double min = r.getThresholdMin();
            switch (r.getConditionType()) {
                case MIN_CONTRACTS:
                    if (c.contractCount < min) return false;
                    break;
                case MIN_REVENUE:
                    if (c.revenue < min) return false;
                    break;
                case MIN_AVG_PER_COMMERCIAL:
                case MIN_AVG_PER_PV:
                    if (c.average == null || c.average < min) return false;
                    break;
                case WEIGHTED_AVG_AGENCY:
                case WEIGHTED_AVG_REGION:
                    if (c.weightedAverage == null || c.weightedAverage < min) return false;
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    /** Picks which Candidate field to sort by: weightedAvg → flatAvg → score. */
    private Comparator<Candidate> getComparator(List<ChallengeWinningRule> rules) {
        if (rules.stream().anyMatch(r ->
                r.getConditionType() == ConditionType.WEIGHTED_AVG_AGENCY ||
                        r.getConditionType() == ConditionType.WEIGHTED_AVG_REGION)) {
            return Comparator.comparing(c -> c.weightedAverage == null ? 0.0 : c.weightedAverage);
        }
        if (rules.stream().anyMatch(r ->
                r.getConditionType() == ConditionType.MIN_AVG_PER_COMMERCIAL ||
                        r.getConditionType() == ConditionType.MIN_AVG_PER_PV)) {
            return Comparator.comparing(c -> c.average == null ? 0.0 : c.average);
        }
        return Comparator.comparing(c -> c.score);
    }

    /** 1 ⇒ weight=1.0; 2 ⇒ 1.5; 3+ ⇒ 2.0 */
    private double computeAgencyWeight(long commCount) {
        if      (commCount >= 3) return 2.0;
        else if (commCount == 2) return 1.5;
        else                     return 1.0;
    }

    /** 1–15 ⇒ 1.0; 16–25 ⇒ 1.5; 26+ ⇒ 2.0 */
    private double computeRegionWeight(long pvCount) {
        if      (pvCount >= 26) return 2.0;
        else if (pvCount >= 16) return 1.5;
        else                    return 1.0;
    }

    /** Internal holder of a user’s full performance & eligibility data. */
    private static class Candidate {
        Long   userId;
        Long   agencyId;
        Long   regionId;
        long   contractCount;
        double revenue;
        double score;
        Double average;
        Double weightedAverage;
    }
}
