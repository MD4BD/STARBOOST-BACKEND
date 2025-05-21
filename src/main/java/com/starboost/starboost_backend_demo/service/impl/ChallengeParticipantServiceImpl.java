package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.entity.Challenge;
import com.starboost.starboost_backend_demo.entity.ChallengeParticipant;
import com.starboost.starboost_backend_demo.entity.ParticipantStatus;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.repository.ChallengeParticipantRepository;
import com.starboost.starboost_backend_demo.repository.ChallengeRepository;
import com.starboost.starboost_backend_demo.repository.UserRepository;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for enrolling participants and
 * offering lookup helpers used during evaluation.
 */
@Service
@RequiredArgsConstructor
public class ChallengeParticipantServiceImpl implements ChallengeParticipantService {
    private final ChallengeParticipantRepository participantRepo;
    private final ChallengeRepository           challengeRepo;
    private final UserRepository                userRepo;

    @Override
    public List<ChallengeParticipantDto> enrollParticipants(Long challengeId, Set<String> targetRoles) {
        // remove old enrollments
        participantRepo.deleteAllByChallengeId(challengeId);
        Challenge challenge = challengeRepo.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found: " + challengeId));

        // enroll all users whose global role is in targetRoles
        List<ChallengeParticipant> participants = userRepo.findAll().stream()
                .filter(u -> targetRoles.contains(u.getRole().name()))
                .map(u -> ChallengeParticipant.builder()
                        .challenge(challenge)
                        .user(u)
                        .role(u.getRole())
                        .agencyId(u.getAgency() != null ? u.getAgency().getId() : null)
                        .regionId(u.getRegion() != null ? u.getRegion().getId() : null)
                        .status(ParticipantStatus.ACTIVE)
                        .build())
                .collect(Collectors.toList());

        participantRepo.saveAll(participants);
        return participants.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChallengeParticipantDto> findByChallengeId(Long challengeId) {
        return participantRepo.findAllByChallengeId(challengeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChallengeParticipantDto findByChallengeAndUser(Long challengeId, Long userId) {
        ChallengeParticipant p = participantRepo
                .findByChallengeIdAndUserId(challengeId, userId)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
        return toDto(p);
    }

    @Override
    public ChallengeParticipantDto findByParticipantId(Long participantId) {
        ChallengeParticipant p = participantRepo.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
        return toDto(p);
    }

    @Override
    public List<ChallengeParticipantDto> findByChallengeAndRole(
            Long challengeId,
            Role role,
            Long filterId,
            String filterName
    ) {
        return findByChallengeId(challengeId).stream()
                .filter(p -> p.getRole() == role)
                .filter(p -> filterId == null
                        || p.getParticipantId().equals(filterId)
                        || p.getUserId().equals(filterId))
                .filter(p -> filterName == null
                        || (p.getFirstName() + " " + p.getLastName())
                        .toLowerCase().contains(filterName.toLowerCase()))
                .collect(Collectors.toList());
    }

    // ─── New helper methods for ChallengeEvaluationServiceImpl ─────────────────

    @Override
    public List<Long> listParticipantIds(Long challengeId, Role roleCategory) {
        // pluck userIds from participants of that role
        return findByChallengeAndRole(challengeId, roleCategory, null, null).stream()
                .map(ChallengeParticipantDto::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public Long getAgencyIdForUser(Long userId) {
        // look up the User’s agency on the User record
        return userRepo.findById(userId)
                .map(u -> u.getAgency() != null ? u.getAgency().getId() : null)
                .orElse(null);
    }

    @Override
    public Long getRegionIdForUser(Long userId) {
        // look up the User’s region on the User record
        return userRepo.findById(userId)
                .map(u -> u.getRegion() != null ? u.getRegion().getId() : null)
                .orElse(null);
    }

    @Override
    public long countByAgency(Long challengeId, Long agencyId) {
        // how many participants share that agency?
        return participantRepo.findAllByChallengeId(challengeId).stream()
                .filter(p -> agencyId != null && agencyId.equals(p.getAgencyId()))
                .count();
    }

    @Override
    public long countByRegion(Long challengeId, Long regionId) {
        // how many participants share that region?
        return participantRepo.findAllByChallengeId(challengeId).stream()
                .filter(p -> regionId != null && regionId.equals(p.getRegionId()))
                .count();
    }

    // ─── Utility: map entity → DTO ────────────────────────────────────
    private ChallengeParticipantDto toDto(ChallengeParticipant p) {
        return ChallengeParticipantDto.builder()
                .participantId(p.getId())
                .challengeId(p.getChallenge().getId())
                .userId(p.getUser().getId())
                .firstName(p.getUser().getFirstName())
                .lastName(p.getUser().getLastName())
                .role(p.getRole())
                .agencyId(p.getAgencyId())
                .regionId(p.getRegionId())
                .status(p.getStatus())
                .build();
    }
}
