package com.starboost.starboost_backend_demo.repository;


import com.starboost.starboost_backend_demo.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
