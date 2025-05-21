package com.starboost.starboost_backend_demo.service;


import com.starboost.starboost_backend_demo.dto.ChallengeDto;
import java.util.List;

public interface ChallengeService {
    List<ChallengeDto> findAll();
    ChallengeDto findById(Long id);
    ChallengeDto create(ChallengeDto dto);
    ChallengeDto update(Long id, ChallengeDto dto);
    void delete(Long id);
}