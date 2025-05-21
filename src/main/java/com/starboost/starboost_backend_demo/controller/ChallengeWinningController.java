package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.WinningRuleDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.service.ChallengeWinningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Exposes “winning rules” (eligibility thresholds) for each challenge,
 * with optional filtering by role category.
 */
@RestController
@RequestMapping(path = "/api/challenges/{challengeId}/winning-rules",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ChallengeWinningController {

    private final ChallengeWinningService winningService;

    /**
     * GET  /api/challenges/{challengeId}/winning-rules
     *
     * List ALL winning-rule definitions for the given challenge.
     *
     * @param challengeId  the ID of the challenge
     * @return a list of WinningRuleDto for every role
     */
    @GetMapping
    public List<WinningRuleDto> listAll(
            @PathVariable Long challengeId
    ) {
        return winningService.listWinningRules(challengeId);
    }

    /**
     * GET  /api/challenges/{challengeId}/winning-rules/{roleCategory}
     *
     * List only the winning rules for a specific role, e.g. AGENT or COMMERCIAL.
     *
     * @param challengeId   the ID of the challenge
     * @param roleCategory  the Role enum to filter by (case‐insensitive)
     * @return a filtered list of WinningRuleDto matching the given roleCategory
     */
    @GetMapping("/{roleCategory}")
    public List<WinningRuleDto> listByRole(
            @PathVariable Long challengeId,
            @PathVariable String roleCategory
    ) {
        // parse the incoming path segment into our Role enum
        Role role = Role.valueOf(roleCategory.toUpperCase());

        // fetch all rules, then filter client-side by roleCategory
        return winningService.listWinningRules(challengeId).stream()
                .filter(dto -> dto.getRoleCategory() == role)
                .collect(Collectors.toList());
    }
}
