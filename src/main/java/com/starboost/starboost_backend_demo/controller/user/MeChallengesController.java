package com.starboost.starboost_backend_demo.controller.user;

import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.dto.ChallengeDto;
import com.starboost.starboost_backend_demo.dto.user.ChallengeSummaryDto;
import com.starboost.starboost_backend_demo.dto.user.ChallengeDetailsDto;
import com.starboost.starboost_backend_demo.dto.user.LeaderboardEntryDto;
import com.starboost.starboost_backend_demo.dto.PerformanceDto;
import com.starboost.starboost_backend_demo.dto.WinnerDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.entity.Product;            // ← imported
import com.starboost.starboost_backend_demo.service.ChallengeEvaluationService;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import com.starboost.starboost_backend_demo.service.ChallengePerformanceService;
import com.starboost.starboost_backend_demo.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/me/challenges")
@RequiredArgsConstructor
public class MeChallengesController {

    private final ChallengeParticipantService participantService;
    private final ChallengePerformanceService performanceService;
    private final ChallengeEvaluationService evaluationService;
    private final ChallengeService challengeService;

    /**
     * GET  /api/me/challenges
     * List the challenges the current user is enrolled in.
     */
    @GetMapping
    public List<ChallengeSummaryDto> myChallenges(
            @AuthenticationPrincipal UserDetails principal
    ) {
        Long userId = participantService.getUserIdByEmail(principal.getUsername());
        return participantService.listChallengeIdsForUser(userId).stream()
                .map(challengeService::findById)       // returns ChallengeDto
                .map(this::mapToSummary)               // map to ChallengeSummaryDto
                .collect(Collectors.toList());
    }

    /**
     * GET  /api/me/challenges/{challengeId}
     * Return full details for this challenge (only if user is enrolled).
     */
    @GetMapping("/{challengeId}")
    public ResponseEntity<ChallengeDetailsDto> myChallengeDetails(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long challengeId
    ) {
        // 1) Find the user’s numeric ID from email
        Long userId = participantService.getUserIdByEmail(principal.getUsername());

        // 2) Verify user is enrolled in the given challenge
        ChallengeParticipantDto part =
                participantService.findByChallengeAndUser(challengeId, userId);
        if (part == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enrolled");
        }

        // 3) Fetch the full ChallengeDto from existing service
        ChallengeDto full = challengeService.findById(challengeId);
        if (full == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found");
        }

        // 4) Map ChallengeDto → ChallengeDetailsDto
        ChallengeDetailsDto details = new ChallengeDetailsDto();
        details.setId(full.getId());
        details.setName(full.getName());
        details.setStartDate(full.getStartDate());
        details.setEndDate(full.getEndDate());
        // full.getStatus() is an enum → call .name() to get String
        details.setStatus(full.getStatus().name());

        // Convert Set<Role> → List<String>
        Set<Role> roleSet = full.getTargetRoles(); // from ChallengeDto
        List<String> roleNames = (roleSet != null)
                ? roleSet.stream().map(Role::name).collect(Collectors.toList())
                : List.of();
        details.setTargetRoles(roleNames);

        // Convert Set<Product> → List<String> by calling toString() on each Product
        Set<Product> prodSet = full.getTargetProducts(); // from ChallengeDto
        List<String> prodNames = (prodSet != null)
                ? prodSet.stream().map(Object::toString).collect(Collectors.toList())
                : List.of();
        details.setTargetProducts(prodNames);

        // Map “rules” (List<RuleDto>) → no conversion needed, but guard null
        details.setRules(full.getRules() != null
                ? full.getRules()
                : List.of());

        // Map “winningRules” (List<WinningRuleDto>)
        details.setWinningRules(full.getWinningRules() != null
                ? full.getWinningRules()
                : List.of());

        // Map “rewardRules” (List<RewardRuleDto>)
        details.setRewardRules(full.getRewardRules() != null
                ? full.getRewardRules()
                : List.of());

        // 5) Return 200 OK + details
        return ResponseEntity.ok(details);
    }

    /**
     * GET  /api/me/challenges/{challengeId}/performance
     * Return this user’s individual performance in the given challenge.
     */
    @GetMapping("/{challengeId}/performance")
    public ResponseEntity<PerformanceDto> myPerformance(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long challengeId
    ) {
        Long userId = participantService.getUserIdByEmail(principal.getUsername());
        ChallengeParticipantDto part =
                participantService.findByChallengeAndUser(challengeId, userId);
        if (part == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enrolled");
        }

        PerformanceDto perf = new PerformanceDto();
        perf.setTotalContracts(
                performanceService.getContractCount(challengeId, userId));
        perf.setTotalRevenue(
                performanceService.getRevenue(challengeId, userId));
        perf.setTotalScore(
                performanceService.getScore(challengeId, userId));
        return ResponseEntity.ok(perf);
    }

    /**
     * GET  /api/me/challenges/{challengeId}/leaderboard
     * Return the leaderboard for the user’s role in this challenge.
     */
    @GetMapping("/{challengeId}/leaderboard")
    public List<LeaderboardEntryDto> myLeaderboard(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long challengeId
    ) {
        Long userId = participantService.getUserIdByEmail(principal.getUsername());
        ChallengeParticipantDto part =
                participantService.findByChallengeAndUser(challengeId, userId);
        if (part == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enrolled");
        }

        Role role = part.getRole();
        List<PerformanceDto> data;
        if (role == Role.AGENT) {
            data = performanceService.agents(challengeId, null, null);
        } else if (role == Role.COMMERCIAL) {
            data = performanceService.commercials(challengeId, null, null);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_IMPLEMENTED,
                    "Leaderboard not supported for role " + role);
        }

        return IntStream.range(0, data.size())
                .mapToObj(i -> {
                    PerformanceDto pd = data.get(i);
                    LeaderboardEntryDto e = new LeaderboardEntryDto();
                    e.setRank(i + 1);
                    e.setUserId(pd.getUserId());
                    e.setFullName(pd.getName());
                    e.setAgencyOrRegion(
                            pd.getAgencyName() != null
                                    ? pd.getAgencyName()
                                    : pd.getRegionName());
                    e.setMetricValue(pd.getTotalScore());
                    return e;
                })
                .collect(Collectors.toList());
    }

    /**
     * GET  /api/me/challenges/{challengeId}/winners
     * Return winners for the user’s role in this challenge.
     */
    @GetMapping("/{challengeId}/winners")
    public List<WinnerDto> myWinners(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long challengeId
    ) {
        Long userId = participantService.getUserIdByEmail(principal.getUsername());
        ChallengeParticipantDto part =
                participantService.findByChallengeAndUser(challengeId, userId);
        if (part == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enrolled");
        }
        return evaluationService.listWinnersByRole(challengeId, part.getRole());
    }

    /** Map full ChallengeDto → lightweight summary for “myChallenges” */
    private ChallengeSummaryDto mapToSummary(ChallengeDto dto) {
        ChallengeSummaryDto sum = new ChallengeSummaryDto();
        sum.setId(dto.getId());
        sum.setName(dto.getName());
        sum.setStartDate(dto.getStartDate());
        sum.setEndDate(dto.getEndDate());
        sum.setStatus(dto.getStatus());
        return sum;
    }
}
