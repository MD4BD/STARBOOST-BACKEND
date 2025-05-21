package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.WinnerDto;
import com.starboost.starboost_backend_demo.entity.Role;

import java.util.List;

/**
 * Runs through all participants, applies winning‐rules, sorts by rank,
 * then applies reward‐rules to produce the final list of winners.
 */
public interface ChallengeEvaluationService {

    /**
     * List **all** winners, across **all** roles, for a given challenge.
     *
     * @param challengeId  the ID of the challenge to evaluate
     * @return a list of WinnerDto, each containing userId, role, rank, reward, etc.
     */
    List<WinnerDto> listWinners(Long challengeId);

    /**
     * List winners for one specific role (e.g. AGENT, COMMERCIAL, AGENCY_MANAGER…).
     *
     * @param challengeId   the ID of the challenge to evaluate
     * @param roleCategory  which Role to evaluate (AGENT, COMMERCIAL, etc.)
     * @return a list of WinnerDto for that single role
     */
    List<WinnerDto> listWinnersByRole(Long challengeId, Role roleCategory);
}
