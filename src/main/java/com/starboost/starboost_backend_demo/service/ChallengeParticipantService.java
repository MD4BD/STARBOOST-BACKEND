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

    Long getUserIdByEmail(String email);
    List<Long> listChallengeIdsForUser(Long userId);





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


    // counts how many commercials in a specific agency in the challenge
    long countCommercialsInAgency(Long challengeId, Long agencyId);


    /**
          * “Points-de-vente” = # of AGENT participants + # of distinct commercial-AGENCIES
          * in a region for this challenge.
     */
    long countSalesPointsInRegion(Long challengeId, Long regionId);
}
