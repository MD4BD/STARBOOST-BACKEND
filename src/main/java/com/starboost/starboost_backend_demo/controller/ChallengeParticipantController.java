package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller exposing both a general roster endpoint and
 * role-scoped participant lookup endpoints.
 */
@RestController
@RequestMapping("/api/challenges/{challengeId}/participants")
@RequiredArgsConstructor
public class ChallengeParticipantController {
    private final ChallengeParticipantService participantService;

    /**
     * GET  /api/challenges/{challengeId}/participants
     * List ALL participants in the challenge, optionally filtered by userId or name.
     */
    @GetMapping
    public ResponseEntity<List<ChallengeParticipantDto>> all(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        // fetch full roster
        List<ChallengeParticipantDto> list = participantService.findByChallengeId(challengeId);

        // filter by userId (or participantId) if provided
        if (filterId != null) {
            list = list.stream()
                    .filter(p -> p.getUserId().equals(filterId)
                            || p.getParticipantId().equals(filterId))
                    .toList();
        }

        // filter by substring match on full name if provided
        if (filterName != null && !filterName.isBlank()) {
            String lower = filterName.toLowerCase();
            list = list.stream()
                    .filter(p -> (p.getFirstName() + " " + p.getLastName())
                            .toLowerCase()
                            .contains(lower))
                    .toList();
        }

        return ResponseEntity.ok(list);
    }

    /**
     * GET /api/challenges/{challengeId}/participants/agents
     * List AGENT participants, optional filtering by ID or name.
     */
    @GetMapping("/agents")
    public ResponseEntity<List<ChallengeParticipantDto>> agents(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(
                participantService.findByChallengeAndRole(
                        challengeId, Role.AGENT, filterId, filterName
                )
        );
    }

    /**
     * GET /api/challenges/{challengeId}/participants/commercials
     * List COMMERCIAL participants, optional filtering by ID or name.
     */
    @GetMapping("/commercials")
    public ResponseEntity<List<ChallengeParticipantDto>> commercials(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(
                participantService.findByChallengeAndRole(
                        challengeId, Role.COMMERCIAL, filterId, filterName
                )
        );
    }

    /**
     * GET /api/challenges/{challengeId}/participants/agency-managers
     * List AGENCY_MANAGER participants, optional filtering.
     */
    @GetMapping("/agency-managers")
    public ResponseEntity<List<ChallengeParticipantDto>> agencyManagers(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(
                participantService.findByChallengeAndRole(
                        challengeId, Role.AGENCY_MANAGER, filterId, filterName
                )
        );
    }

    /**
     * GET /api/challenges/{challengeId}/participants/regional-managers
     * List REGIONAL_MANAGER participants, optional filtering.
     */
    @GetMapping("/regional-managers")
    public ResponseEntity<List<ChallengeParticipantDto>> regionalManagers(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(
                participantService.findByChallengeAndRole(
                        challengeId, Role.REGIONAL_MANAGER, filterId, filterName
                )
        );
    }

    /**
     * GET /api/challenges/{challengeId}/participants/animators
     * List ANIMATOR participants, optional filtering.
     */
    @GetMapping("/animators")
    public ResponseEntity<List<ChallengeParticipantDto>> animators(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(
                participantService.findByChallengeAndRole(
                        challengeId, Role.ANIMATOR, filterId, filterName
                )
        );
    }
}
