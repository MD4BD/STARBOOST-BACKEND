package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.ScoreRuleDto;
import java.util.List;

public interface ScoreRuleService {
    List<ScoreRuleDto> findAllByChallengeId(Long challengeId);
    ScoreRuleDto      findById(Long challengeId, Long ruleId);
    ScoreRuleDto      createForChallenge(Long challengeId, ScoreRuleDto dto);
    ScoreRuleDto      updateForChallenge(Long challengeId, Long ruleId, ScoreRuleDto dto);
    void              deleteForChallenge(Long challengeId, Long ruleId);
}