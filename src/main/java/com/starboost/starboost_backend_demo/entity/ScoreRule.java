package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Defines one scoring rule: type, conditions, and point values.
 */
@Entity
@Table(name = "score_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** What dimension to score on: CONTRACT, PACK, or REVENUE */
    @Enumerated(EnumType.STRING)
    private ScoreType scoreType;

    /** Optional: which contract type triggers this rule */
    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    /** Optional: which pack type triggers this rule */
    @Enumerated(EnumType.STRING)
    private PackType packType;

    /** Points awarded per event (contract) */
    private Integer points;

    /** Revenue divisor for REVENUE‐type rules */
    private Integer revenueUnit;

    /** Link back to the Challenge owning this rule */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;
}
