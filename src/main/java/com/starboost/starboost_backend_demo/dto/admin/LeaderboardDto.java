// src/main/java/com/starboost/starboost_backend_demo/dto/admin/LeaderboardDto.java
package com.starboost.starboost_backend_demo.dto.admin;

import lombok.Data;

/** A simplified version of ChallengeLeaderboardDto for admin metrics */
@Data
public class LeaderboardDto {
    private Long userId;
    private String name;
    private int score;
}
