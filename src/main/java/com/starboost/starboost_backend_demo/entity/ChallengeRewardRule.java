package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Defines the payout tiers and amounts for each role in a challenge.
 */
@Entity
@Table(name = "challenge_reward_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeRewardRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ← back‐pointer so Hibernate can fill the FK column on insert
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_category", nullable = false)
    private Role roleCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "payout_type", nullable = false)
    private PayoutType payoutType;    // FIXED_TIERS or PERCENT_TIERS or RANK_TIERS

    @Column(name = "tier_min", nullable = false)
    private Double tierMin;   // Base threshold per unit (e.g. per-commercial or per-PV)

    @Column(name = "tier_max", nullable = false)
    private Double tierMax;   // Upper bound of threshold range

    @Column(name = "base_amount", nullable = false)
    private Double baseAmount;   // Fixed gift or percent value

    @Column(name = "gift", nullable = true)
    private String gift;
}

