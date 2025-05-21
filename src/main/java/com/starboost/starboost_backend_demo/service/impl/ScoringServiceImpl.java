// src/main/java/com/starboost/starboost_backend_demo/service/impl/ScoringServiceImpl.java
package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.entity.Challenge;
import com.starboost.starboost_backend_demo.entity.SalesTransaction;
import com.starboost.starboost_backend_demo.entity.ScoreRule;
import com.starboost.starboost_backend_demo.repository.ChallengeRepository;
import com.starboost.starboost_backend_demo.repository.SalesTransactionRepository;
import com.starboost.starboost_backend_demo.repository.ScoreRuleRepository;
import com.starboost.starboost_backend_demo.service.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Concrete ScoringServiceImpl that:
 *  1) fetches only this challenge’s ScoreRules,
 *  2) fetches only this challenge’s SalesTransactions,
 *  3) filters by the challenge’s time window & filter‐rules,
 *  4) applies each ScoreRule (CONTRACT, PACK, REVENUE),
 *  5) accumulates per sellerId and returns Map<userId,score>.
 */
@Service
@RequiredArgsConstructor
public class ScoringServiceImpl implements ScoringService {

    private final ChallengeRepository        challengeRepo;
    private final SalesTransactionRepository txRepo;
    private final ScoreRuleRepository        scoreRuleRepo;

    @Override
    public Map<Long, Integer> calculateScores(Long challengeId) {
        // 1) load challenge for its dates & filter rules
        Challenge challenge = challengeRepo.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found: " + challengeId));
        LocalDateTime start = challenge.getStartDate().atStartOfDay();
        LocalDateTime end   = challenge.getEndDate().plusDays(1).atStartOfDay();

        // 2) load only rules for this challenge
        List<ScoreRule> scoreRules = scoreRuleRepo.findAllByChallenge_Id(challengeId);
        System.out.println(">>> SCORES FOR CHALLENGE "
                + challengeId + " → " + scoreRules);

        // —— QUICK SANITY CHECK ——
        // fetch _all_ transactions, before date‐filtering
        List<SalesTransaction> allTxs = txRepo.findAllByChallenge_Id(challengeId);
        System.out.println("ALL TXS for challenge " + challengeId + " → " + allTxs);

        // 3) now apply your date‐window filter
        List<SalesTransaction> txs = allTxs.stream()
                .filter(t -> !t.getSaleDate().isBefore(start) && t.getSaleDate().isBefore(end))
                .toList();
        System.out.println("FILTERED TXS for challenge " + challengeId + " → " + txs);

        Map<Long,Integer> scores = new HashMap<>();

        for (SalesTransaction tx : txs) {
            // 4) enforce challenge‐level filters (contractType, transactionNature, packType)
            boolean matches = challenge.getRules().stream().anyMatch(r ->
                    (r.getContractType()      == null || r.getContractType()      == tx.getContractType()) &&
                            (r.getTransactionNature() == null || r.getTransactionNature() == tx.getTransactionNature()) &&
                            (r.getPackType()          == null /*|| pack‐mapping if you’ve wired it*/)
            );
            if (!matches) continue;

            // 5) compute points for this transaction
            int txScore = 0;
            for (ScoreRule sr : scoreRules) {
                switch (sr.getScoreType()) {
                    case CONTRACT:
                        if (sr.getContractType() == tx.getContractType()) {
                            txScore += sr.getPoints();
                        }
                        break;
                    case PACK:
                        // apply when you’ve mapped pack‐types
                        break;
                    case REVENUE:
                        txScore += (int)((tx.getPremium() / sr.getRevenueUnit()) * sr.getPoints());
                        break;
                }
            }

            // 6) accumulate per sellerId
            scores.merge(tx.getSellerId(), txScore, Integer::sum);
        }

        System.out.println("FINAL SCORES for challenge " + challengeId + " → " + scores);
        return scores;
    }
}
