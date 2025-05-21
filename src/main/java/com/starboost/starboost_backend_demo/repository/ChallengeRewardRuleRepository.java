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
     * Matches the entity’s challenge.id via underscore syntax.
     */
    List<ChallengeRewardRule> findAllByChallenge_IdAndRoleCategory(
            Long challengeId,
            Role roleCategory
    );

    /**
     * Fetch all reward rules for a given challenge.
     */
    List<ChallengeRewardRule> findAllByChallenge_Id(Long challengeId);
}
