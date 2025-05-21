package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.ScoreRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScoreRuleRepository extends JpaRepository<ScoreRule, Long> {
    List<ScoreRule> findAllByChallengeId(Long challengeId);
}