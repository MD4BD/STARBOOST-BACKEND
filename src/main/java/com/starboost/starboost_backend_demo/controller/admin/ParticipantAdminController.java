// src/main/java/com/starboost/starboost_backend_demo/controller/admin/ParticipantAdminController.java
package com.starboost.starboost_backend_demo.controller.admin;

import com.starboost.starboost_backend_demo.dto.admin.ParticipantCreateDto;
import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/challenges/{challengeId}/participants")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ParticipantAdminController {

    private final ChallengeParticipantService participantService;

    /**
     * List all participants in a challenge
     */
    @GetMapping
    public List<ChallengeParticipantDto> listAll(@PathVariable Long challengeId) {
        return participantService.findByChallengeId(challengeId);
    }

}