package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Defines the qualifying ("winning") thresholds for each role in a challenge.
 */
@Entity
@Table(name = "challenge_winning_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeWinningRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Back‐reference to the parent Challenge */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    /** Which role this rule applies to (AGENT, COMMERCIAL, AGENCY_MANAGER, etc.) */
    @Enumerated(EnumType.STRING)
    @Column(name = "role_category", nullable = false)
    private Role roleCategory;

    /**
     * The low‐water mark for this gate:
     * - For MIN_CONTRACTS / MIN_REVENUE / MIN_AVG_…: the minimum you must reach.
     * - For weighted‐avg: also ignored (we use formulaDetails).
     */
    @Column(name = "threshold_min", nullable = false)
    private Double thresholdMin;


    /**
     * What kind of gate this is:
     * MIN_CONTRACTS, MIN_REVENUE, MIN_AVG_PER_COMMERCIAL, MIN_AVG_PER_PV,
     * WEIGHTED_AVG_AGENCY, or WEIGHTED_AVG_REGION.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type", nullable = false)
    private ConditionType conditionType;

    /**
     * JSON‐encoded details for weighted‐avg gates only (WEIGHTED_AVG_AGENCY/
     * WEIGHTED_AVG_REGION).  Null for flat gates.
     * Example:
     * {
     *   "tiers": [
     *     {"min":1,"max":1,"weight":1.0},
     *     {"min":2,"max":2,"weight":1.5},
     *     {"min":3,"max":999,"weight":2.0}
     *   ],
     *   "unit":"COMMERCIAL_COUNT"
     * }
     */
    @Lob
    @Column(name = "formula_details")
    private String formulaDetails;
}
