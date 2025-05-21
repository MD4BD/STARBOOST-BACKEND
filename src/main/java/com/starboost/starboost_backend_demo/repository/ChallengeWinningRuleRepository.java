package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.ChallengeWinningRule;
import com.starboost.starboost_backend_demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for fetching “winning” (eligibility) rules of a challenge.
 */
@Repository
public interface ChallengeWinningRuleRepository
        extends JpaRepository<ChallengeWinningRule, Long> {

    /**
     * All rules for a challenge.
     */
    List<ChallengeWinningRule> findAllByChallengeId(Long challengeId);

    /**
     * Only the rules for one role within that challenge.
     */
    List<ChallengeWinningRule> findAllByChallengeIdAndRoleCategory(
            Long challengeId,
            Role roleCategory
    );
}
