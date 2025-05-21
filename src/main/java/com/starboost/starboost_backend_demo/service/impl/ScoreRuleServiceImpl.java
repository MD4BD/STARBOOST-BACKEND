package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.ScoreRuleDto;
import com.starboost.starboost_backend_demo.entity.Challenge;
import com.starboost.starboost_backend_demo.entity.ScoreRule;
import com.starboost.starboost_backend_demo.repository.ChallengeRepository;
import com.starboost.starboost_backend_demo.repository.ScoreRuleRepository;
import com.starboost.starboost_backend_demo.service.ScoreRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreRuleServiceImpl implements ScoreRuleService {
    private final ScoreRuleRepository    repo;
    private final ChallengeRepository    challRepo;

    @Override
    public List<ScoreRuleDto> findAllByChallengeId(Long challengeId) {
        return repo.findAllByChallengeId(challengeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ScoreRuleDto findById(Long challengeId, Long ruleId) {
        ScoreRule sr = repo.findById(ruleId)
                .filter(r -> r.getChallenge().getId().equals(challengeId))
                .orElseThrow(() -> new RuntimeException("ScoreRule not found for challenge"));
        return toDto(sr);
    }

    @Override
    public ScoreRuleDto createForChallenge(Long challengeId, ScoreRuleDto dto) {
        Challenge chall = challRepo.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found"));
        ScoreRule sr = toEntity(dto);
        sr.setChallenge(chall);
        ScoreRule saved = repo.save(sr);
        return toDto(saved);
    }

    @Override
    public ScoreRuleDto updateForChallenge(Long challengeId, Long ruleId, ScoreRuleDto dto) {
        ScoreRule existing = repo.findById(ruleId)
                .filter(r -> r.getChallenge().getId().equals(challengeId))
                .orElseThrow(() -> new RuntimeException("ScoreRule not found for challenge"));
        existing.setScoreType(dto.getScoreType());
        existing.setContractType(dto.getContractType());
        existing.setPackType(dto.getPackType());
        existing.setPoints(dto.getPoints());
        existing.setRevenueUnit(dto.getRevenueUnit());
        ScoreRule updated = repo.save(existing);
        return toDto(updated);
    }

    @Override
    public void deleteForChallenge(Long challengeId, Long ruleId) {
        ScoreRule existing = repo.findById(ruleId)
                .filter(r -> r.getChallenge().getId().equals(challengeId))
                .orElseThrow(() -> new RuntimeException("ScoreRule not found for challenge"));
        repo.delete(existing);
    }

    private ScoreRuleDto toDto(ScoreRule sr) {
        return ScoreRuleDto.builder()
                .id(sr.getId())
                .scoreType(sr.getScoreType())
                .contractType(sr.getContractType())
                .packType(sr.getPackType())
                .points(sr.getPoints())
                .revenueUnit(sr.getRevenueUnit())
                .challengeId(sr.getChallenge().getId())
                .build();
    }

    private ScoreRule toEntity(ScoreRuleDto d) {
        return ScoreRule.builder()
                .scoreType(d.getScoreType())
                .contractType(d.getContractType())
                .packType(d.getPackType())
                .points(d.getPoints())
                .revenueUnit(d.getRevenueUnit())
                .build();
    }
}