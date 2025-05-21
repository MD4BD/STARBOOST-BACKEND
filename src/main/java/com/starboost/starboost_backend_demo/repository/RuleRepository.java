package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    // you may add findAllByChallengeId(...) later
}