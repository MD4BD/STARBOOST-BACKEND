// src/main/java/com/starboost/starboost_backend_demo/dto/user/LeaderboardEntryDto.java
package com.starboost.starboost_backend_demo.dto.user;

import lombok.Data;

@Data
public class LeaderboardEntryDto {
    private int rank;
    private Long userId;
    private String fullName;
    private String agencyOrRegion;   // whatever applies
    private double metricValue;      // score, revenue, etc.
}
