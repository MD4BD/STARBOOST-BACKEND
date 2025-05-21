package com.starboost.starboost_backend_demo.dto;

import com.starboost.starboost_backend_demo.entity.Role;
import lombok.*;

/**
 * Aggregated performance metrics for a challenge participant or entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceDto {
    private Long participantId;   // for agents & commercials; null for agencies/regions
    private Long challengeId;
    private Long userId;          // for agents & commercials; null for agencies/regions
    private String name;          // first+last for people; branch/region name for agencies/regions
    private Role role;            // AGENT, COMMERCIAL, AGENCY, REGION
    private Long agencyId;        // for individual/commercials/agencies; null for regions
    private Long regionId;

    private int totalContracts;
    private double totalRevenue;
    private int totalScore;
    private int rank;
}