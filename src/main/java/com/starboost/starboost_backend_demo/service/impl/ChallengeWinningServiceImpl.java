package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.WinningRuleDto;
import com.starboost.starboost_backend_demo.repository.ChallengeWinningRuleRepository;
import com.starboost.starboost_backend_demo.service.ChallengeWinningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ChallengeWinningService.
 * Fetches ChallengeWinningRule entities and maps them to DTOs.
 */
@Service
@RequiredArgsConstructor
public class ChallengeWinningServiceImpl implements ChallengeWinningService {
    private final ChallengeWinningRuleRepository repo;

    /**
     * List all winning rules (thresholds/conditions) for a given challenge.
     *
     * @param challengeId the ID of the challenge
     * @return a List of WinningRuleDto with typed elements
     */
    @Override
    public List<WinningRuleDto> listWinningRules(Long challengeId) {
        return repo.findAllByChallengeId(challengeId).stream()
                // Map each entity to its DTO
                .map(rule -> WinningRuleDto.builder()
                        .id(rule.getId())
                        .roleCategory(rule.getRoleCategory())
                        .conditionType(rule.getConditionType())
                        .thresholdMin(rule.getThresholdMin())
                        .build()
                )
                // Collect into a List<WinningRuleDto>
                .collect(Collectors.toList());
    }
}
