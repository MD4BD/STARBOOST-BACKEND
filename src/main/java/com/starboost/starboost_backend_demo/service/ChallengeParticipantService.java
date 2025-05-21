package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * Service interface for managing and querying challenge participants.
 */
public interface ChallengeParticipantService {
    List<ChallengeParticipantDto> enrollParticipants(Long challengeId, Set<String> targetRoles);
    List<ChallengeParticipantDto> findByChallengeId(Long challengeId);
    ChallengeParticipantDto findByChallengeAndUser(Long challengeId, Long userId);
    ChallengeParticipantDto findByParticipantId(Long participantId);
    List<ChallengeParticipantDto> findByChallengeAndRole(
            Long challengeId,
            Role role,
            Long filterId,
            String filterName
    );

    /**
     * For evaluation: list the user‐IDs of all participants in one role.
     */
    List<Long> listParticipantIds(Long challengeId, Role roleCategory);

    /**
     * For evaluation: lookup the user’s agency (if any).
     */
    Long getAgencyIdForUser(Long userId);

    /**
     * For evaluation: lookup the user’s region.
     */
    Long getRegionIdForUser(Long userId);

    /**
     * For evaluation: how many participants in this challenge share that agency?
     */
    long countByAgency(Long challengeId, Long agencyId);

    /**
     * For evaluation: how many participants in this challenge share that region?
     */
    long countByRegion(Long challengeId, Long regionId);
}
