package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.PerformanceDto;

import java.util.List;

/**
 * Service for fetching raw performance metrics:
 *   - agent / commercial / agency / region level
 *   - and granular helpers used during evaluation.
 */
public interface ChallengePerformanceService {

    /** List each agent’s metrics and ranking. */
    List<PerformanceDto> agents(Long challengeId,
                                Long filterUserId,
                                String filterName);

    /** List each commercial’s metrics and ranking. */
    List<PerformanceDto> commercials(Long challengeId,
                                     Long filterUserId,
                                     String filterName);

    /** List each agency’s aggregated metrics and ranking. */
    List<PerformanceDto> agencies(Long challengeId,
                                  Long filterAgencyId,
                                  String filterName);

    /** List each region’s aggregated metrics and ranking. */
    List<PerformanceDto> regions(Long challengeId,
                                 Long filterRegionId,
                                 String filterName);

    // ─── Granular helpers used by ChallengeEvaluationServiceImpl ────────

    /** How many contracts this user signed in the challenge. */
    long getContractCount(Long challengeId, Long userId);

    /** How much total revenue this user generated in the challenge. */
    double getRevenue(Long challengeId, Long userId);

    /** What raw score this user earned (from your ScoringService). */
    int getScore(Long challengeId, Long userId);

    /** Sum of all revenues for this agency’s commercials. */
    double getAgencyTotalRevenue(Long challengeId, Long agencyId);

    /** Sum of all raw scores for this agency’s commercials. */
    int getAgencyTotalScore(Long challengeId, Long agencyId);

    /** Sum of all contract counts for this agency’s commercials. */
    long getAgencyTotalContracts(Long challengeId, Long agencyId);

    /** Sum of all revenues for this region’s sales points. */
    double getRegionTotalRevenue(Long challengeId, Long regionId);

    /** Sum of all raw scores for this region’s sales points. */
    int getRegionTotalScore(Long challengeId, Long regionId);

    /** Sum of all contracts for this region’s sales points. */
    long getRegionTotalContracts(Long challengeId, Long regionId);






}