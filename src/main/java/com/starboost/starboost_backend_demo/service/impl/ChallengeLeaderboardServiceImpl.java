package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.ChallengeLeaderboardDto;
import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.entity.Agency;
import com.starboost.starboost_backend_demo.entity.Region;
import com.starboost.starboost_backend_demo.repository.AgencyRepository;
import com.starboost.starboost_backend_demo.repository.RegionRepository;
import com.starboost.starboost_backend_demo.service.ChallengeLeaderboardService;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import com.starboost.starboost_backend_demo.service.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeLeaderboardServiceImpl implements ChallengeLeaderboardService {
    private final ChallengeParticipantService participantService;
    private final ScoringService              scoringService;
    private final AgencyRepository             agencyRepo;
    private final RegionRepository             regionRepo;

    @Override
    public List<ChallengeLeaderboardDto> agents(Long challengeId) {
        Map<Long, Integer> scores = scoringService.calculateScores(challengeId);
        List<ChallengeLeaderboardDto> list = participantService.findByChallengeId(challengeId).stream()
                .filter(p -> "AGENT".equals(p.getRole().name()))
                .map(p -> ChallengeLeaderboardDto.builder()
                        .rank(0)
                        .userId(p.getUserId())
                        .name(p.getFirstName() + " " + p.getLastName())
                        .role("AGENT")
                        .agencyId(p.getAgencyId())
                        .regionId(p.getRegionId())
                        .score(scores.getOrDefault(p.getUserId(), 0))
                        .build())
                .collect(Collectors.toList());
        return assignRanks(list);
    }

    @Override
    public List<ChallengeLeaderboardDto> commercials(Long challengeId) {
        Map<Long, Integer> scores = scoringService.calculateScores(challengeId);
        List<ChallengeLeaderboardDto> list = participantService.findByChallengeId(challengeId).stream()
                .filter(p -> "COMMERCIAL".equals(p.getRole().name()))
                .map(p -> ChallengeLeaderboardDto.builder()
                        .rank(0)
                        .userId(p.getUserId())
                        .name(p.getFirstName() + " " + p.getLastName())
                        .role("COMMERCIAL")
                        .agencyId(p.getAgencyId())
                        .regionId(p.getRegionId())
                        .score(scores.getOrDefault(p.getUserId(), 0))
                        .build())
                .collect(Collectors.toList());
        return assignRanks(list);
    }

    @Override
    public List<ChallengeLeaderboardDto> agencies(Long challengeId) {
        Map<Long, Integer> scores = scoringService.calculateScores(challengeId);
        Map<Long, List<ChallengeParticipantDto>> byAgency = participantService.findByChallengeId(challengeId).stream()
                .filter(p -> "COMMERCIAL".equals(p.getRole().name()) && p.getAgencyId() != null)
                .collect(Collectors.groupingBy(ChallengeParticipantDto::getAgencyId));

        List<ChallengeLeaderboardDto> list = new ArrayList<>();
        for (var entry : byAgency.entrySet()) {
            Long aid = entry.getKey();
            int totalScore = entry.getValue().stream()
                    .mapToInt(p -> scores.getOrDefault(p.getUserId(), 0))
                    .sum();
            Agency agency = agencyRepo.findById(aid).orElseThrow();
            list.add(ChallengeLeaderboardDto.builder()
                    .rank(0)
                    .userId(null)
                    .name(agency.getName())
                    .role("AGENCY")
                    .agencyId(aid)
                    .regionId(agency.getRegion().getId())
                    .score(totalScore)
                    .build());
        }
        return assignRanks(list);
    }

    @Override
    public List<ChallengeLeaderboardDto> regions(Long challengeId) {
        Map<Long, Integer> scores = scoringService.calculateScores(challengeId);
        Map<Long, List<ChallengeParticipantDto>> byRegion = participantService.findByChallengeId(challengeId).stream()
                .filter(p -> p.getRegionId() != null)
                .collect(Collectors.groupingBy(ChallengeParticipantDto::getRegionId));

        List<ChallengeLeaderboardDto> list = new ArrayList<>();
        for (var entry : byRegion.entrySet()) {
            Long rid = entry.getKey();
            int totalScore = entry.getValue().stream()
                    .mapToInt(p -> scores.getOrDefault(p.getUserId(), 0))
                    .sum();
            Region region = regionRepo.findById(rid).orElseThrow();
            list.add(ChallengeLeaderboardDto.builder()
                    .rank(0)
                    .userId(null)
                    .name(region.getName())
                    .role("REGION")
                    .agencyId(null)
                    .regionId(rid)
                    .score(totalScore)
                    .build());
        }
        return assignRanks(list);
    }

    // assign ascending rank based on score desc
    private List<ChallengeLeaderboardDto> assignRanks(List<ChallengeLeaderboardDto> list) {
        Collections.sort(list, Comparator.comparingInt(ChallengeLeaderboardDto::getScore).reversed());
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRank(i + 1);
        }
        return list;
    }
}
