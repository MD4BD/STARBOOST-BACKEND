// src/main/java/com/starboost/starboost_backend_demo/controller/admin/AdminMetricsController.java
package com.starboost.starboost_backend_demo.controller.admin;

import com.starboost.starboost_backend_demo.dto.WinnerDto;
import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.dto.PerformanceDto;
import com.starboost.starboost_backend_demo.service.ChallengeEvaluationService;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import com.starboost.starboost_backend_demo.service.ChallengePerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminMetricsController {

    private final ChallengeEvaluationService evalService;
    private final ChallengeParticipantService partService;
    private final ChallengePerformanceService perfService;

    /** All winners across roles */
    @GetMapping("/leaderboards")
    public List<WinnerDto> leaderboards(@RequestParam Long challengeId) {
        return evalService.listWinners(challengeId);
    }

    /** Full participant roster */
    @GetMapping("/participants")
    public List<ChallengeParticipantDto> participants(@RequestParam Long challengeId) {
        return partService.findByChallengeId(challengeId);
    }

    /** Performance metrics (agents by default) */
    @GetMapping("/performance")
    public List<PerformanceDto> performance(@RequestParam Long challengeId) {
        // you can expose separate endpoints for agencies/regions if needed
        return perfService.agents(challengeId, null, null);
    }
}
