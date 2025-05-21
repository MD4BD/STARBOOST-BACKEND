package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {
    List<ChallengeParticipant> findAllByChallengeId(Long challengeId);
    Optional<ChallengeParticipant> findByChallengeIdAndUserId(Long challengeId, Long userId);

    // Clear all participants for a given challenge (needed for re-enrollment)
    void deleteAllByChallengeId(Long challengeId);
}
