package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for SalesTransaction entities.
 * Contains challenge-scoped lookup methods for Performance and Scoring pipelines.
 */
@Repository
public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Long> {

    /**
     * Fetch all transactions for a given challenge (by challenge.id).
     */
    List<SalesTransaction> findAllByChallenge_Id(Long challengeId);

    /**
     * Count how many transactions a given seller made in a specific challenge.
     */
    long countByChallenge_IdAndSellerId(Long challengeId, Long sellerId);

    /**
     * Sum the premiums of all transactions by a given seller in a specific challenge.
     * Uses JPQL with COALESCE to return 0.0 if no rows found.
     */
    @Query("""
        SELECT COALESCE(SUM(t.premium), 0)
        FROM SalesTransaction t
        WHERE t.challenge.id = :challengeId
          AND t.sellerId      = :sellerId
    """)
    double sumPremiumByChallengeIdAndSellerId(
            @Param("challengeId") Long challengeId,
            @Param("sellerId")    Long sellerId
    );
}
