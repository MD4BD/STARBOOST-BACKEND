package com.starboost.starboost_backend_demo.dto;

import com.starboost.starboost_backend_demo.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * Aggregated performance metrics for a challenge participant or entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerformanceDto {
    private Long participantId;   // for agents & commercials; null for agencies/regions
    private Long challengeId;
    private Long userId;          // for agents & commercials; null for agencies/regions
    private String name;          // first+last for people; branch/region name for agencies/regions
    private Role role;
    private Long agencyId;
    private String agencyName;
    private Long regionId;
    private String regionName;

    private long totalContracts;
    private double totalRevenue;
    private int totalScore;
    private int rank;
}