package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.WinningRuleDto;
import java.util.List;

/**
 * Service for listing the winning rules of a challenge.
 */
public interface ChallengeWinningService {

    /**
     * Return all winning‐rule DTOs for the given challenge.
     *
     * @param challengeId ID of the challenge whose rules we want
     * @return a typed List of WinningRuleDto
     */
    List<WinningRuleDto> listWinningRules(Long challengeId);
}
