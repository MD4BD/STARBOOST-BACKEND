package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.ChallengeRewardRule;
import com.starboost.starboost_backend_demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for challenge-scoped reward rules.
 */
@Repository
public interface ChallengeRewardRuleRepository
        extends JpaRepository<ChallengeRewardRule, Long> {

    /**
     * Fetch all reward rules for a given challenge and role category.
     *
     * @param challengeId  the ID of the challenge
     * @param roleCategory the role (e.g. AGENT, COMMERCIAL, AGENCY_MANAGER…)
     * @return a list of matching ChallengeRewardRule entities
     */
    List<ChallengeRewardRule> findAllByChallengeIdAndRoleCategory(
            Long challengeId,
            Role roleCategory
    );

    List<ChallengeRewardRule> findAllByChallengeId(Long challengeId);


}
