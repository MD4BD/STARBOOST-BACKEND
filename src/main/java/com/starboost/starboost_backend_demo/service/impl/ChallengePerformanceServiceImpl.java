package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.dto.PerformanceDto;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import com.starboost.starboost_backend_demo.service.ChallengePerformanceService;
import com.starboost.starboost_backend_demo.service.ScoringService;
import com.starboost.starboost_backend_demo.repository.SalesTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default implementation of ChallengePerformanceService.
 * Builds up all the raw metrics (contracts, revenue, score) for agents,
 * commercials, agencies and regions, and exposes helper methods used during evaluation.
 */
@Service
@RequiredArgsConstructor
public class ChallengePerformanceServiceImpl implements ChallengePerformanceService {
    private final ChallengeParticipantService participantService;
    private final SalesTransactionRepository   salesRepo;
    private final ScoringService               scoringService;

    @Override
    public List<PerformanceDto> agents(Long challengeId, Long filterUserId, String filterName) {
        return buildPerformance(
                participantService.findByChallengeId(challengeId).stream()
                        .filter(p -> p.getRole().name().equals("AGENT")),
                challengeId, filterUserId, filterName
        );
    }

    @Override
    public List<PerformanceDto> commercials(Long challengeId, Long filterUserId, String filterName) {
        return buildPerformance(
                participantService.findByChallengeId(challengeId).stream()
                        .filter(p -> p.getRole().name().equals("COMMERCIAL")),
                challengeId, filterUserId, filterName
        );
    }

    @Override
    public List<PerformanceDto> agencies(Long challengeId, Long filterAgencyId, String filterName) {
        // group commercials by agency and aggregate
        Map<Long, List<ChallengeParticipantDto>> byAgency = participantService.findByChallengeId(challengeId)
                .stream()
                .filter(p -> p.getRole().name().equals("COMMERCIAL"))
                .collect(Collectors.groupingBy(ChallengeParticipantDto::getAgencyId));

        List<PerformanceDto> list = new ArrayList<>();
        Map<Long, Integer>   scores = scoringService.calculateScores(challengeId);

        for (var entry : byAgency.entrySet()) {
            Long agencyId = entry.getKey();
            int contracts = 0;
            double revenue = 0;
            int score     = 0;

            // sum each commercial’s metrics
            for (var p : entry.getValue()) {
                contracts += salesRepo.countByChallengeIdAndSellerId(challengeId, p.getUserId());
                revenue   += salesRepo.sumPremiumByChallengeIdAndSellerId(challengeId, p.getUserId());
                score     += scores.getOrDefault(p.getUserId(), 0);
            }

            list.add(PerformanceDto.builder()
                    .participantId(null)
                    .challengeId(challengeId)
                    .userId(null)
                    .name("Agency " + agencyId)     // or fetch real branch name
                    .role(null)
                    .agencyId(agencyId)
                    .regionId(entry.getValue().get(0).getRegionId())
                    .totalContracts(contracts)
                    .totalRevenue(revenue)
                    .totalScore(score)
                    .rank(0)
                    .build()
            );
        }

        return finalize(list, filterAgencyId, filterName);
    }

    @Override
    public List<PerformanceDto> regions(Long challengeId, Long filterRegionId, String filterName) {
        // group agents+commercials by region and aggregate
        Map<Long, List<ChallengeParticipantDto>> byRegion = participantService.findByChallengeId(challengeId)
                .stream()
                .filter(p -> p.getRole().name().equals("AGENT")
                        || p.getRole().name().equals("COMMERCIAL"))
                .collect(Collectors.groupingBy(ChallengeParticipantDto::getRegionId));

        List<PerformanceDto> list = new ArrayList<>();
        Map<Long, Integer>   scores = scoringService.calculateScores(challengeId);

        for (var entry : byRegion.entrySet()) {
            Long regionId = entry.getKey();
            int contracts = 0;
            double revenue = 0;
            int score     = 0;

            // sum each participant’s metrics
            for (var p : entry.getValue()) {
                contracts += salesRepo.countByChallengeIdAndSellerId(challengeId, p.getUserId());
                revenue   += salesRepo.sumPremiumByChallengeIdAndSellerId(challengeId, p.getUserId());
                score     += scores.getOrDefault(p.getUserId(), 0);
            }

            list.add(PerformanceDto.builder()
                    .participantId(null)
                    .challengeId(challengeId)
                    .userId(null)
                    .name("Region " + regionId)
                    .role(null)
                    .agencyId(null)
                    .regionId(regionId)
                    .totalContracts(contracts)
                    .totalRevenue(revenue)
                    .totalScore(score)
                    .rank(0)
                    .build()
            );
        }

        return finalize(list, filterRegionId, filterName);
    }

    // ─── Granular helpers used by ChallengeEvaluationServiceImpl ────────

    @Override
    public long getContractCount(Long challengeId, Long userId) {
        return salesRepo.countByChallengeIdAndSellerId(challengeId, userId);
    }

    @Override
    public double getRevenue(Long challengeId, Long userId) {
        return salesRepo.sumPremiumByChallengeIdAndSellerId(challengeId, userId);
    }

    @Override
    public int getScore(Long challengeId, Long userId) {
        return scoringService.calculateScores(challengeId)
                .getOrDefault(userId, 0);
    }

    @Override
    public double getAgencyTotalRevenue(Long challengeId, Long agencyId) {
        return participantService.findByChallengeId(challengeId).stream()
                .filter(p -> p.getRole().name().equals("COMMERCIAL"))
                .filter(p -> Objects.equals(agencyId, p.getAgencyId()))
                .mapToDouble(p -> salesRepo.sumPremiumByChallengeIdAndSellerId(challengeId, p.getUserId()))
                .sum();
    }

    @Override
    public int getAgencyTotalScore(Long challengeId, Long agencyId) {
        Map<Long,Integer> scores = scoringService.calculateScores(challengeId);
        return participantService.findByChallengeId(challengeId).stream()
                .filter(p -> p.getRole().name().equals("COMMERCIAL"))
                .filter(p -> Objects.equals(agencyId, p.getAgencyId()))
                .mapToInt(p -> scores.getOrDefault(p.getUserId(), 0))
                .sum();
    }

    @Override
    public double getRegionTotalRevenue(Long challengeId, Long regionId) {
        return participantService.findByChallengeId(challengeId).stream()
                .filter(p -> p.getRole().name().equals("AGENT")
                        || p.getRole().name().equals("COMMERCIAL"))
                .filter(p -> Objects.equals(regionId, p.getRegionId()))
                .mapToDouble(p -> salesRepo.sumPremiumByChallengeIdAndSellerId(challengeId, p.getUserId()))
                .sum();
    }

    @Override
    public int getRegionTotalScore(Long challengeId, Long regionId) {
        Map<Long,Integer> scores = scoringService.calculateScores(challengeId);
        return participantService.findByChallengeId(challengeId).stream()
                .filter(p -> p.getRole().name().equals("AGENT")
                        || p.getRole().name().equals("COMMERCIAL"))
                .filter(p -> Objects.equals(regionId, p.getRegionId()))
                .mapToInt(p -> scores.getOrDefault(p.getUserId(), 0))
                .sum();
    }

    @Override
    public long getRegionPVCount(Long challengeId, Long regionId) {
        // “points de vente” count = number of participants in that region
        return participantService.findByChallengeId(challengeId).stream()
                .filter(p -> Objects.equals(regionId, p.getRegionId()))
                .count();
    }

    // ─── Private helpers to build, sort & filter a list ─────────────────

    private List<PerformanceDto> buildPerformance(
            Stream<ChallengeParticipantDto> participants,
            Long challengeId,
            Long filterId,
            String filterName
    ) {
        var list   = participants.collect(Collectors.toList());
        var scores = scoringService.calculateScores(challengeId);
        List<PerformanceDto> dtoList = new ArrayList<>();

        for (var p : list) {
            int    contracts = (int) salesRepo.countByChallengeIdAndSellerId(challengeId, p.getUserId());
            double revenue   = salesRepo.sumPremiumByChallengeIdAndSellerId(challengeId, p.getUserId());
            int    score     = scores.getOrDefault(p.getUserId(), 0);
            dtoList.add(PerformanceDto.builder()
                    .participantId(p.getParticipantId())
                    .challengeId(challengeId)
                    .userId(p.getUserId())
                    .name(p.getFirstName() + " " + p.getLastName())
                    .role(p.getRole())
                    .agencyId(p.getAgencyId())
                    .regionId(p.getRegionId())
                    .totalContracts(contracts)
                    .totalRevenue(revenue)
                    .totalScore(score)
                    .rank(0)
                    .build());
        }
        return finalize(dtoList, filterId, filterName);
    }

    private List<PerformanceDto> finalize(
            List<PerformanceDto> list,
            Long filterId,
            String filterName
    ) {
        // sort descending by total score
        list.sort(Comparator.comparingInt(PerformanceDto::getTotalScore).reversed());
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRank(i + 1);
        }
        // apply optional filters
        if (filterId != null) {
            list = list.stream()
                    .filter(d -> Objects.equals(filterId, d.getParticipantId())
                            || Objects.equals(filterId, d.getUserId()))
                    .collect(Collectors.toList());
        }
        if (filterName != null && !filterName.isBlank()) {
            String low = filterName.toLowerCase();
            list = list.stream()
                    .filter(d -> d.getName().toLowerCase().contains(low))
                    .collect(Collectors.toList());
        }
        return list;
    }
}
