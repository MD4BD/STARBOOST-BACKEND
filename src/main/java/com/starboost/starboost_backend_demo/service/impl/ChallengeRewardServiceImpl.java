package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.RewardRuleDto;
import com.starboost.starboost_backend_demo.entity.ChallengeRewardRule;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.repository.ChallengeRewardRuleRepository;
import com.starboost.starboost_backend_demo.service.ChallengeRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of ChallengeRewardService.
 *
 * - listRewardRules: fetches reward‐rule tiers from the repository, maps to DTOs.
 * - computeReward: filters those tiers by comparing metricValue against tierMin/tierMax × unitCount.
 */
@Service
@RequiredArgsConstructor
public class ChallengeRewardServiceImpl implements ChallengeRewardService {
    private final ChallengeRewardRuleRepository repo;

    @Override
    public List<RewardRuleDto> listRewardRules(Long challengeId, Role roleCategory) {
        return repo.findAllByChallengeIdAndRoleCategory(challengeId, roleCategory).stream()
                .map(r -> RewardRuleDto.builder()
                        .id(r.getId())
                        .roleCategory(r.getRoleCategory())
                        .payoutType(r.getPayoutType())
                        .tierMin(r.getTierMin())
                        .tierMax(r.getTierMax())
                        .baseAmount(r.getBaseAmount())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<RewardRuleDto> listAllRewardRules(Long challengeId) {
        return repo.findAllByChallengeId(challengeId).stream()
                .map(r -> toDto(r))
                .collect(Collectors.toList());
    }

    @Override
    public double computeReward(Long challengeId, Role roleCategory, Long unitCount, double metricValue) {
        return repo.findAllByChallengeIdAndRoleCategory(challengeId, roleCategory).stream()
                // find the first tier whose thresholds (min/max) × unitCount bracket the metricValue
                .filter(r -> metricValue >= r.getTierMin() * unitCount
                        && metricValue <= r.getTierMax() * unitCount)
                .findFirst()
                .map(ChallengeRewardRule::getBaseAmount)
                .orElse(0.0);
    }

    private RewardRuleDto toDto(ChallengeRewardRule r) {
        return RewardRuleDto.builder()
                .id(r.getId())
                .roleCategory(r.getRoleCategory())
                .payoutType(r.getPayoutType())
                .tierMin(r.getTierMin())
                .tierMax(r.getTierMax())
                .baseAmount(r.getBaseAmount())
                .build();
    }
}
