// src/main/java/com/starboost/starboost_backend_demo/dto/WinningRuleDto.java
package com.starboost.starboost_backend_demo.dto;

import lombok.*;
import com.starboost.starboost_backend_demo.entity.ConditionType;
import com.starboost.starboost_backend_demo.entity.Role;

/**
 * DTO exposing a single winning‐rule to the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WinningRuleDto {
    private Long id;
    private Role roleCategory;

    /**
     * Which gate this is (flat or weighted):
     * MIN_CONTRACTS, MIN_REVENUE, MIN_AVG_PER_COMMERCIAL, MIN_AVG_PER_PV,
     * WEIGHTED_AVG_AGENCY, or WEIGHTED_AVG_REGION.
     */
    private ConditionType conditionType;

    /** Flat‐gate minimum. */
    private Double thresholdMin;


    /**
     * JSON blob for weighted‐avg tier definitions.
     * Null for flat gates.
     */
    private String formulaDetails;
}
