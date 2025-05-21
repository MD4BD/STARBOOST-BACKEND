package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.entity.*;
import com.starboost.starboost_backend_demo.repository.*;
import com.starboost.starboost_backend_demo.service.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoringServiceImpl implements ScoringService {

    private final ChallengeRepository        challengeRepo;
    private final SalesTransactionRepository txRepo;
    private final ScoreRuleRepository        scoreRuleRepo;

    @Override
    public Map<Long, Integer> calculateScores(Long challengeId) {
        Challenge challenge = challengeRepo.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found"));

        // 1) Load all ScoreRules
        List<ScoreRule> scoreRules = scoreRuleRepo.findAll();

        // 2) Fetch all transactions in [start, end)
        LocalDateTime start = challenge.getStartDate().atStartOfDay();
        LocalDateTime end   = challenge.getEndDate().plusDays(1).atStartOfDay();
        List<SalesTransaction> txs = txRepo.findAll().stream()
                .filter(t -> !t.getSaleDate().isBefore(start) && t.getSaleDate().isBefore(end))
                .collect(Collectors.toList());

        Map<Long, Integer> scores = new HashMap<>();

        for (SalesTransaction tx : txs) {
            // 3) Only include if it matches at least one Rule
            boolean matches = challenge.getRules().stream().anyMatch(r ->
                    (r.getContractType() == null      || r.getContractType() == tx.getContractType()) &&
                            (r.getTransactionNature() == null || r.getTransactionNature() == tx.getTransactionNature()) &&
                            (r.getPackType() == null          /*|| r.getPackType() == <mapProductToPack>(tx.getProduct())*/ )
            );
            if (!matches) continue;

            // 4) Sum up points from each ScoreRule
            int txScore = 0;
            for (ScoreRule sr : scoreRules) {
                switch (sr.getScoreType()) {
                    case CONTRACT:
                        if (sr.getContractType() == tx.getContractType()) {
                            txScore += sr.getPoints();
                        }
                        break;
                    case PACK:
                        // if you add a PackType on SalesTransaction, compare here:
                        // PackType p = <mapProductToPack>(tx.getProduct());
                        // if (sr.getPackType() == p) txScore += sr.getPoints();
                        break;
                    case REVENUE:
                        txScore += (int)((tx.getPremium() / sr.getRevenueUnit()) * sr.getPoints());
                        break;
                }
            }

            // 5) Accumulate per sellerId
            scores.merge(tx.getSellerId(), txScore, Integer::sum);
        }

        return scores;
    }
}