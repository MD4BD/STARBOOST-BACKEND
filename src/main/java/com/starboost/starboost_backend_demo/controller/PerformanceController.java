package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.PerformanceDto;
import com.starboost.starboost_backend_demo.service.ChallengePerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges/{challengeId}/performance")
@RequiredArgsConstructor
public class PerformanceController {
    private final ChallengePerformanceService performanceService;

    @GetMapping("/agents")
    public ResponseEntity<List<PerformanceDto>> agents(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(performanceService.agents(challengeId, filterId, filterName));
    }

    @GetMapping("/commercials")
    public ResponseEntity<List<PerformanceDto>> commercials(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(performanceService.commercials(challengeId, filterId, filterName));
    }

    @GetMapping("/agencies")
    public ResponseEntity<List<PerformanceDto>> agencies(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(performanceService.agencies(challengeId, filterId, filterName));
    }

    @GetMapping("/regions")
    public ResponseEntity<List<PerformanceDto>> regions(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(performanceService.regions(challengeId, filterId, filterName));
    }
}