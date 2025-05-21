package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.*;
import com.starboost.starboost_backend_demo.entity.*;
import com.starboost.starboost_backend_demo.repository.ChallengeRepository;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import com.starboost.starboost_backend_demo.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for creating, updating, deleting, and fetching
 * Challenge definitions, including their score, winning, and reward rules.
 */
@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository repo;
    private final ChallengeParticipantService participantService;

    @Override
    public List<ChallengeDto> findAll() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChallengeDto findById(Long id) {
        Challenge c = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Challenge not found: " + id));
        return toDto(c);
    }

    @Override
    public ChallengeDto create(ChallengeDto dto) {
        // 1) DTO → entity
        Challenge entity = toEntity(dto);

        // 2) persist (cascades child rules)
        Challenge saved = repo.save(entity);

        // 3) enroll participants
        participantService.enrollParticipants(
                saved.getId(),
                dto.getTargetRoles().stream().map(Enum::name).collect(Collectors.toSet())
        );

        // 4) return DTO
        return toDto(saved);
    }

    @Override
    public ChallengeDto update(Long id, ChallengeDto dto) {
        Challenge existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Challenge not found: " + id));

        // --- basics ---
        existing.setName(dto.getName());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setTargetRoles(dto.getTargetRoles());
        existing.setTargetProducts(dto.getTargetProducts());
        existing.setStarBoostType(dto.getStarBoostType());
        existing.setStarBoostValue(dto.getStarBoostValue());
        existing.setStatus(dto.getStatus());
        existing.setDeleted(dto.isDeleted());

        // --- score rules (back-pointer still present) ---
        existing.getRules().clear();
        if (dto.getRules() != null) {
            List<Rule> rules = dto.getRules().stream()
                    .map(rd -> Rule.builder()
                            .contractType(rd.getContractType())
                            .transactionNature(rd.getTransactionNature())
                            .packType(rd.getPackType())
                            .challenge(existing)
                            .build())
                    .collect(Collectors.toList());
            existing.getRules().addAll(rules);
        }

        // --- winning rules (now bidirectional & only min threshold) ---
        existing.getWinningRules().clear();
        if (dto.getWinningRules() != null) {
            List<ChallengeWinningRule> wr = dto.getWinningRules().stream()
                    .map(wd -> ChallengeWinningRule.builder()
                            .challenge(existing)              // back-pointer so FK is set
                            .roleCategory(wd.getRoleCategory())
                            .conditionType(wd.getConditionType())
                            .thresholdMin(wd.getThresholdMin())
                            .build())
                    .collect(Collectors.toList());
            existing.getWinningRules().addAll(wr);
        }

        // --- reward rules (bidirectional) ---
        existing.getRewardRules().clear();
        if (dto.getRewardRules() != null) {
            List<ChallengeRewardRule> rr = dto.getRewardRules().stream()
                    .map(rd -> ChallengeRewardRule.builder()
                            .challenge(existing)              // back-pointer
                            .roleCategory(rd.getRoleCategory())
                            .payoutType(rd.getPayoutType())
                            .tierMin(rd.getTierMin())
                            .tierMax(rd.getTierMax())
                            .baseAmount(rd.getBaseAmount())
                            .build())
                    .collect(Collectors.toList());
            existing.getRewardRules().addAll(rr);
        }

        // 5) persist changes
        Challenge saved = repo.save(existing);

        // 6) re-enroll if roles changed
        participantService.enrollParticipants(
                id,
                dto.getTargetRoles().stream().map(Enum::name).collect(Collectors.toSet())
        );

        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ─── MAPPERS ─────────────────────────────────────────

    private ChallengeDto toDto(Challenge c) {
        var ruleDtos = c.getRules().stream()
                .map(r -> RuleDto.builder()
                        .id(r.getId())
                        .contractType(r.getContractType())
                        .transactionNature(r.getTransactionNature())
                        .packType(r.getPackType())
                        .build())
                .collect(Collectors.toList());

        // only thresholdMin is exposed now
        var winDtos = c.getWinningRules().stream()
                .map(w -> WinningRuleDto.builder()
                        .id(w.getId())
                        .roleCategory(w.getRoleCategory())
                        .conditionType(w.getConditionType())
                        .thresholdMin(w.getThresholdMin())
                        .build())
                .collect(Collectors.toList());

        var rewardDtos = c.getRewardRules().stream()
                .map(r -> RewardRuleDto.builder()
                        .id(r.getId())
                        .roleCategory(r.getRoleCategory())
                        .payoutType(r.getPayoutType())
                        .tierMin(r.getTierMin())
                        .tierMax(r.getTierMax())
                        .baseAmount(r.getBaseAmount())
                        .build())
                .collect(Collectors.toList());

        return ChallengeDto.builder()
                .id(c.getId())
                .name(c.getName())
                .startDate(c.getStartDate())
                .endDate(c.getEndDate())
                .targetRoles(c.getTargetRoles())
                .targetProducts(c.getTargetProducts())
                .rules(ruleDtos)
                .winningRules(winDtos)
                .rewardRules(rewardDtos)
                .starBoostType(c.getStarBoostType())
                .starBoostValue(c.getStarBoostValue())
                .status(c.getStatus())
                .deleted(c.isDeleted())
                .build();
    }

    private Challenge toEntity(ChallengeDto d) {
        Challenge chall = Challenge.builder()
                .name(d.getName())
                .startDate(d.getStartDate())
                .endDate(d.getEndDate())
                .targetRoles(d.getTargetRoles())
                .targetProducts(d.getTargetProducts())
                .starBoostType(d.getStarBoostType())
                .starBoostValue(d.getStarBoostValue())
                .status(d.getStatus())
                .deleted(d.isDeleted())
                .build();

        if (d.getRules() != null) {
            var rules = d.getRules().stream()
                    .map(rd -> Rule.builder()
                            .contractType(rd.getContractType())
                            .transactionNature(rd.getTransactionNature())
                            .packType(rd.getPackType())
                            .challenge(chall)
                            .build())
                    .collect(Collectors.toList());
            chall.setRules(rules);
        }

        if (d.getWinningRules() != null) {
            var wr = d.getWinningRules().stream()
                    .map(wd -> ChallengeWinningRule.builder()
                            .challenge(chall)
                            .roleCategory(wd.getRoleCategory())
                            .conditionType(wd.getConditionType())
                            .thresholdMin(wd.getThresholdMin())
                            .build())
                    .collect(Collectors.toList());
            chall.setWinningRules(wr);
        }

        if (d.getRewardRules() != null) {
            var rr = d.getRewardRules().stream()
                    .map(rd -> ChallengeRewardRule.builder()
                            .challenge(chall)
                            .roleCategory(rd.getRoleCategory())
                            .payoutType(rd.getPayoutType())
                            .tierMin(rd.getTierMin())
                            .tierMax(rd.getTierMax())
                            .baseAmount(rd.getBaseAmount())
                            .build())
                    .collect(Collectors.toList());
            chall.setRewardRules(rr);
        }

        return chall;
    }
}
