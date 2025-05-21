// src/main/java/com/starboost/starboost_backend_demo/controller/admin/WinnersAdminController.java
package com.starboost.starboost_backend_demo.controller.admin;

import com.starboost.starboost_backend_demo.dto.WinnerDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.service.ChallengeEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/challenges/{challengeId}/winners")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class WinnersAdminController {

    private final ChallengeEvaluationService evaluationService;

    /** All winners across roles */
    @GetMapping
    public List<WinnerDto> listAll(@PathVariable Long challengeId) {
        return evaluationService.listWinners(challengeId);
    }

    /** Winners filtered by role */
    @GetMapping(params = "role")
    public List<WinnerDto> listByRole(
            @PathVariable Long challengeId,
            @RequestParam Role role
    ) {
        return evaluationService.listWinnersByRole(challengeId, role);
    }
}
