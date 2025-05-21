// src/main/java/com/starboost/starboost_backend_demo/service/impl/ChallengePerformanceServiceImpl.java
package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.dto.PerformanceDto;
import com.starboost.starboost_backend_demo.entity.Agency;
import com.starboost.starboost_backend_demo.entity.Region;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.repository.AgencyRepository;
import com.starboost.starboost_backend_demo.repository.RegionRepository;
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
 * Builds raw metrics for agents, commercials, agencies, and regions,
 * and provides helper methods for evaluation.
 */
@Service
@RequiredArgsConstructor
public class ChallengePerformanceServiceImpl implements ChallengePerformanceService {

    private final ChallengeParticipantService participantService;
    private final SalesTransactionRepository   salesRepo;
    private final ScoringService               scoringService;
    private final AgencyRepository             agencyRepo;
    private final RegionRepository             regionRepo;

    @Override
    public List<PerformanceDto> agents(Long challengeId, Long filterUserId, String filterName) {
        // Only AGENT participants
        return buildPerformance(
                participantService.findByChallengeId(challengeId).stream()
                        .filter(p -> p.getRole().name().equals("AGENT")),
                challengeId, filterUserId, filterName
        );
    }

    @Override
    public List<PerformanceDto> commercials(Long challengeId, Long filterUserId, String filterName) {
        // Only COMMERCIAL participants
        return buildPerformance(
                participantService.findByChallengeId(challengeId).stream()
                        .filter(p -> p.getRole().name().equals("COMMERCIAL")),
                challengeId, filterUserId, filterName
        );
    }

    @Override
    public List<PerformanceDto> agencies(Long challengeId, Long filterAgencyId, String filterName) {
        // 1) Group only COMMERCIAL participants by agency
        Map<Long,List<ChallengeParticipantDto>> byAgency = participantService.findByChallengeId(challengeId)
                .stream()
                .filter(p -> p.getRole()==Role.COMMERCIAL)
                .collect(Collectors.groupingBy(ChallengeParticipantDto::getAgencyId));

        List<PerformanceDto> result = new ArrayList<>();
        // Precompute scores once
        Map<Long,Integer> scores = scoringService.calculateScores(challengeId);

        // 2) For each agency, fetch the three aggregates from your service layer
        for (var entry : byAgency.entrySet()) {
            Long agencyId = entry.getKey();

            // Call your own aggregation methods:
            long totalContracts = getAgencyTotalContracts(challengeId, agencyId);
            double totalRevenue = getAgencyTotalRevenue(challengeId, agencyId);
            int totalScore      = getAgencyTotalScore(challengeId, agencyId);

            // Fetch the human-readable agency name
            Agency ag = agencyRepo.findById(agencyId).orElse(null);
            String agencyName = ag != null ? ag.getName() : null;


            // Derive regionId from any one of the commercials in this agency
            Long regionId = entry.getValue().isEmpty()
                    ? null
                    : entry.getValue().get(0).getRegionId();

            String regionName = regionRepo.findById(regionId)
                               .map(Region::getName)
                               .orElse(null);

            // 3) Build and collect the DTO
            result.add(PerformanceDto.builder()
                    .participantId(null)
                    .challengeId(challengeId)
                    .userId(null)
                    .name(agencyName)
                    .role(null)
                    .agencyId(agencyId)
                    .regionId(regionId)
                    .regionName(regionName)
                    .totalContracts(totalContracts)
                    .totalRevenue(totalRevenue)
                    .totalScore(totalScore)
                    .rank(0)
                    .build());
        }

        // 4) Apply filtering/ranking
        return finalize(result, filterAgencyId, filterName);
    }

    @Override
    public List<PerformanceDto> regions(Long challengeId, Long filterRegionId, String filterName) {
        // 1) Group AGENT+COMMERCIAL participants by region
        Map<Long,List<ChallengeParticipantDto>> byRegion = participantService.findByChallengeId(challengeId)
                .stream()
                .filter(p -> p.getRole()==Role.AGENT || p.getRole()==Role.COMMERCIAL)
                .collect(Collectors.groupingBy(ChallengeParticipantDto::getRegionId));

        List<PerformanceDto> result = new ArrayList<>();
        Map<Long,Integer> scores = scoringService.calculateScores(challengeId);

        for (var entry : byRegion.entrySet()) {
            Long regionId = entry.getKey();

            // 2) Call *this* service’s aggregation methods directly:
            long totalContracts = getRegionTotalContracts(challengeId, regionId);
            double totalRevenue = getRegionTotalRevenue(challengeId, regionId);
            int totalScore      = getRegionTotalScore(challengeId, regionId);

            // 3) Look up the region name
            Region rg = regionRepo.findById(regionId).orElse(null);
            String regionName = rg != null ? rg.getName() : null;

            // 4) Build the DTO
            result.add(PerformanceDto.builder()
                    .participantId(null)
                    .challengeId(challengeId)
                    .userId(null)
                    .name(regionName)
                    .role(null)
                    .agencyId(null)
                    .regionId(regionId)
                    .totalContracts(totalContracts)
                    .totalRevenue(totalRevenue)
                    .totalScore(totalScore)
                    .rank(0)
                    .build());
        }

        return finalize(result, filterRegionId, filterName);
    }



    @Override
    public long getContractCount(Long challengeId, Long userId) {
        return salesRepo.countByChallenge_IdAndSellerId(challengeId, userId);
    }

    @Override
    public double getRevenue(Long challengeId, Long userId) {
        return salesRepo.sumPremiumByChallengeIdAndSellerId(challengeId, userId);
    }

    @Override
    public int getScore(Long challengeId, Long userId) {
        return scoringService.calculateScores(challengeId).getOrDefault(userId, 0);
    }

    @Override
    public double getAgencyTotalRevenue(Long challengeId, Long agencyId) {
        return participantService.findByChallengeId(challengeId).stream()
                .filter(p -> p.getRole().name().equals("COMMERCIAL") && Objects.equals(agencyId, p.getAgencyId()))
                .mapToDouble(p -> salesRepo.sumPremiumByChallengeIdAndSellerId(challengeId, p.getUserId()))
                .sum();
    }

    @Override
    public int getAgencyTotalScore(Long challengeId, Long agencyId) {
        Map<Long,Integer> scores = scoringService.calculateScores(challengeId);
        return participantService.findByChallengeId(challengeId).stream()
                .filter(p -> p.getRole().name().equals("COMMERCIAL") && Objects.equals(agencyId, p.getAgencyId()))
                .mapToInt(p -> scores.getOrDefault(p.getUserId(), 0))
                .sum();
    }

    @Override
    public long getAgencyTotalContracts(Long challengeId, Long agencyId) {
        return participantService.findByChallengeId(challengeId).stream()
                .filter(p -> p.getRole().name().equals("COMMERCIAL") && Objects.equals(agencyId, p.getAgencyId()))
                .mapToLong(p -> salesRepo.countByChallenge_IdAndSellerId(challengeId, p.getUserId()))
                .sum();
    }

    @Override
    public double getRegionTotalRevenue(Long challengeId, Long regionId) {
        return participantService.findByChallengeId(challengeId).stream()
                .filter(p -> (p.getRole().name().equals("AGENT") || p.getRole().name().equals("COMMERCIAL"))
                        && Objects.equals(regionId, p.getRegionId()))
                .mapToDouble(p -> salesRepo.sumPremiumByChallengeIdAndSellerId(challengeId, p.getUserId()))
                .sum();
    }

    @Override
    public int getRegionTotalScore(Long challengeId, Long regionId) {
        Map<Long,Integer> scores = scoringService.calculateScores(challengeId);
        return participantService.findByChallengeId(challengeId).stream()
                .filter(p -> (p.getRole().name().equals("AGENT") || p.getRole().name().equals("COMMERCIAL"))
                        && Objects.equals(regionId, p.getRegionId()))
                .mapToInt(p -> scores.getOrDefault(p.getUserId(), 0))
                .sum();
    }

    @Override
    public long getRegionTotalContracts(Long challengeId, Long regionId) {
        // 1) List all Agents in this challenge/region
        List<Long> agentIds =
                participantService.listParticipantIds(challengeId, Role.AGENT);

        // 2) List all Commercials in this challenge/region
        List<Long> commercialIds =
                participantService.listParticipantIds(challengeId, Role.COMMERCIAL);

        // 3) Combine, filter by regionId, sum each user's contract count
        return Stream.concat(agentIds.stream(), commercialIds.stream())
                .filter(userId -> Objects.equals(
                        regionId,
                        participantService.getRegionIdForUser(userId)))
                .mapToLong(userId ->
                        getContractCount(challengeId, userId))
                .sum();
    }


    // ─── private helpers ───────────────────────────────────────────────────

    private List<PerformanceDto> buildPerformance(
            Stream<ChallengeParticipantDto> participants,
            Long challengeId,
            Long filterId,
            String filterName
    ) {
        List<ChallengeParticipantDto> list = participants.collect(Collectors.toList());
        Map<Long,Integer> scores = scoringService.calculateScores(challengeId);
        List<PerformanceDto> dtos = new ArrayList<>();

        for (var p : list) {
            long cCount = getContractCount(challengeId, p.getUserId());
            double rev = salesRepo.sumPremiumByChallengeIdAndSellerId(challengeId, p.getUserId());
            int scr = scores.getOrDefault(p.getUserId(), 0);

            String regionName = null;
            if (p.getRegionId() != null) {
                var rg = regionRepo.findById(p.getRegionId()).orElse(null);
                regionName = (rg != null ? rg.getName() : null);
            }

            String agencyName = null;
            if (p.getRole() == Role.COMMERCIAL && p.getAgencyId() != null) {
                agencyName = agencyRepo.findById(p.getAgencyId())
                        .map(Agency::getName)
                        .orElse(null);
            }

            dtos.add(PerformanceDto.builder()
                    .participantId(p.getParticipantId())
                    .challengeId(challengeId)
                    .userId(p.getUserId())
                    .name(p.getFirstName() + " " + p.getLastName())
                    .role(p.getRole())
                    .agencyId(p.getAgencyId())
                    .agencyName(agencyName)
                    .regionId(p.getRegionId())
                    .regionName(regionName)
                    .totalContracts(cCount)
                    .totalRevenue(rev)
                    .totalScore(scr)
                    .rank(0)
                    .build());
        }

        return finalize(dtos, filterId, filterName);
    }

    private List<PerformanceDto> finalize(List<PerformanceDto> list, Long filterId, String filterName) {
        // sort by score desc & assign ranks
        list.sort(Comparator.comparingInt(PerformanceDto::getTotalScore).reversed());
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRank(i + 1);
        }
        // optional ID filter
        if (filterId != null) {
            list = list.stream()
                    .filter(d -> Objects.equals(filterId, d.getParticipantId())
                            || Objects.equals(filterId, d.getUserId()))
                    .collect(Collectors.toList());
        }
        // optional name filter
        if (filterName != null && !filterName.isBlank()) {
            String low = filterName.toLowerCase();
            list = list.stream()
                    .filter(d -> d.getName().toLowerCase().contains(low))
                    .collect(Collectors.toList());
        }
        return list;
    }
}
