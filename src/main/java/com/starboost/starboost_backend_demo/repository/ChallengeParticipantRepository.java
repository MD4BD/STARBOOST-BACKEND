package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.ChallengeParticipant;
import com.starboost.starboost_backend_demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ChallengeParticipant entities, scoped by challenge.
 */
@Repository
public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {

    /**
     * Fetch all participants for a given challenge.
     */
    List<ChallengeParticipant> findAllByChallenge_Id(Long challengeId);

    /**
     * Fetch all participants for a given challenge and role.
     */
    List<ChallengeParticipant> findAllByChallenge_IdAndRole(Long challengeId, Role role);

    /**
     * Find the one participant record for a given user (across any challenge).
     */
    Optional<ChallengeParticipant> findByUserId(Long userId);

    /**
     * How many participants (commercials) in this agency for this challenge (agency managers not included).
     */
    long countByChallenge_IdAndAgencyIdAndRole(
            Long challengeId, Long agencyId, Role role);

    // counts users with a given role
    long countByChallenge_IdAndRegionIdAndRole(
            Long challengeId, Long regionId, Role role);


    /**
     * How many participants in this agency for this challenge.
     */
    long countByChallenge_IdAndAgencyId(Long challengeId, Long agencyId);

    /**
     * How many participants in this region for this challenge.
     */
    long countByChallenge_IdAndRegionId(Long challengeId, Long regionId);

    // ─── NEW METHODS ────────────────────────────────────

    /**
     * Delete all participants for a challenge (used when removing a challenge).
     */
    void deleteAllByChallenge_Id(Long challengeId);

    /**
     * Fetch the single participant record matching challenge + user.
     */
    Optional<ChallengeParticipant> findByChallenge_IdAndUserId(Long challengeId, Long userId);

    List<ChallengeParticipant> findAllByUser_Id(Long userId);

}
