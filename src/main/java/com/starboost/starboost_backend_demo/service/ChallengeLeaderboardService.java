package com.starboost.starboost_backend_demo.service;


import com.starboost.starboost_backend_demo.dto.ChallengeLeaderboardDto;
import java.util.List;

public interface ChallengeLeaderboardService {
    List<ChallengeLeaderboardDto> agents(Long challengeId);
    List<ChallengeLeaderboardDto> commercials(Long challengeId);
    List<ChallengeLeaderboardDto> agencies(Long challengeId);
    List<ChallengeLeaderboardDto> regions(Long challengeId);
}