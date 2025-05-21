package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.SalesTransactionDto;
import com.starboost.starboost_backend_demo.service.SalesTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges/{challengeId}/sales")
@RequiredArgsConstructor
public class SalesTransactionController {
    private final SalesTransactionService service;

    @PostMapping
    public ResponseEntity<SalesTransactionDto> create(@PathVariable Long challengeId,
                                                      @RequestBody SalesTransactionDto dto) {
        SalesTransactionDto created =
                service.createForChallenge(challengeId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<SalesTransactionDto>> getAll(@PathVariable Long challengeId) {
        return ResponseEntity.ok(
                service.findAllByChallengeId(challengeId));
    }
}