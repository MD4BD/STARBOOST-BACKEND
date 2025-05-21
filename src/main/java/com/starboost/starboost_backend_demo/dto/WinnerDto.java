package com.starboost.starboost_backend_demo.dto;

import com.starboost.starboost_backend_demo.entity.Role;
import lombok.*;

/**
 * Represents one “winner” in a challenge:
 *  – who they are (userId, agencyId, regionId),
 *  – their rank,
 *  – their raw metrics (contracts, revenue, score, averages),
 *  – and their final payout.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WinnerDto {
    /** ID of the user who won */
    private Long userId;

    /** Their top-level role in the challenge (AGENT, COMMERCIAL, etc.) */
    private Role roleCategory;

    /** If they belong to an agency, that agency’s ID; else null */
    private Long agencyId;

    /** Their home region ID (for everyone) */
    private Long regionId;

    /** Their rank within their role (1 for top, 2, 3…) */
    private int rank;

    /** Raw counts & scores used in evaluation */
    private long contractCount;
    private double revenue;
    private double score;

    /** For agency/region roles, the flat average they achieved */
    private Double average;

    /** For weighted‐average roles, the weighted average they achieved */
    private Double weightedAverage;

    /** The payout they earned (fixed amount or percent‐of‐revenue) */
    private double rewardAmount;
}
