package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for SalesTransaction entities.
 * Contains only challenge-scoped lookup methods for our Performance and Scoring pipelines.
 */
@Repository
public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Long> {

    /**
     * Fetch all sales transactions belonging to a specific challenge.
     */
    List<SalesTransaction> findAllByChallengeId(Long challengeId);

    /**
     * Count how many transactions a given seller made in a specific challenge.
     */
    long countByChallengeIdAndSellerId(Long challengeId, Long sellerId);

    /**
     * Sum the premiums of all transactions made by a given seller in a specific challenge.
     * Returns 0.0 if none exist.
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
