package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.PerformanceDto;
import com.starboost.starboost_backend_demo.service.ChallengePerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import java.util.stream.Collectors;
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
        // 1) get the full list
        List<PerformanceDto> list = performanceService.agents(challengeId, filterId, filterName);

        // 2) apply id filter (agentId == userId in the DTO)
        if (filterId != null) {
            list = list.stream()
                    .filter(d -> filterId.equals(d.getUserId()))
                    .collect(Collectors.toList());
        }

        // 3) apply name filter
        if (filterName != null && !filterName.isBlank()) {
            String lower = filterName.toLowerCase();
            list = list.stream()
                    .filter(d -> d.getName() != null
                            && d.getName().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("/commercials")
    public ResponseEntity<List<PerformanceDto>> commercials(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        List<PerformanceDto> list = performanceService.commercials(challengeId, filterId, filterName);

        if (filterId != null) {
            list = list.stream()
                    .filter(d -> filterId.equals(d.getUserId()))
                    .collect(Collectors.toList());
        }

        if (filterName != null && !filterName.isBlank()) {
            String lower = filterName.toLowerCase();
            list = list.stream()
                    .filter(d -> d.getName() != null
                            && d.getName().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("/agencies")
    public ResponseEntity<List<PerformanceDto>> agencies(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        List<PerformanceDto> list = performanceService.agencies(challengeId, filterId, filterName);

        if (filterId != null) {
            list = list.stream()
                    .filter(d -> filterId.equals(d.getAgencyId()))
                    .collect(Collectors.toList());
        }

        if (filterName != null && !filterName.isBlank()) {
            String lower = filterName.toLowerCase();
            list = list.stream()
                    .filter(d -> d.getName() != null
                            && d.getName().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("/regions")
    public ResponseEntity<List<PerformanceDto>> regions(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        List<PerformanceDto> list = performanceService.regions(challengeId, filterId, filterName);

        if (filterId != null) {
            list = list.stream()
                    .filter(d -> filterId.equals(d.getRegionId()))
                    .collect(Collectors.toList());
        }

        if (filterName != null && !filterName.isBlank()) {
            String lower = filterName.toLowerCase();
            list = list.stream()
                    .filter(d -> d.getName() != null
                            && d.getName().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(list);
    }
}
