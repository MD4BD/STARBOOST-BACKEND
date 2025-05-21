package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.ScoreRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for ScoreRule entities, scoped by challenge.
 */
@Repository
public interface ScoreRuleRepository extends JpaRepository<ScoreRule, Long> {

    /**
     * Fetch only rules belonging to this specific challenge (by challenge.id).
     */
    List<ScoreRule> findAllByChallenge_Id(Long challengeId);
}