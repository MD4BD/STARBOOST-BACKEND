package com.starboost.starboost_backend_demo.service;

import java.util.Map;

public interface ScoringService {
    /**
     * Calculate total score per participantId for the given challenge.
     */
    Map<Long, Integer> calculateScores(Long challengeId);
}
