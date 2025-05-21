// src/main/java/com/starboost/starboost_backend_demo/service/impl/ChallengeWinningServiceImpl.java
package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.WinningRuleDto;
import com.starboost.starboost_backend_demo.entity.ChallengeWinningRule;
import com.starboost.starboost_backend_demo.repository.ChallengeWinningRuleRepository;
import com.starboost.starboost_backend_demo.service.ChallengeWinningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Fetches all winning‐rule thresholds for a given challenge.
 */
@Service
@RequiredArgsConstructor
public class ChallengeWinningServiceImpl implements ChallengeWinningService {
    private final ChallengeWinningRuleRepository repo;

    @Override
    public List<WinningRuleDto> listWinningRules(Long challengeId) {
        // fetch every winning rule for this challenge
        return repo.findAllByChallenge_Id(challengeId).stream()
                .map(rule -> WinningRuleDto.builder()
                        .id(rule.getId())
                        .roleCategory(rule.getRoleCategory())
                        .conditionType(rule.getConditionType())
                        .thresholdMin(rule.getThresholdMin())
                        .build())
                .toList();
    }
}
