// src/main/java/com/starboost/starboost_backend_demo/entity/Challenge.java
package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.*;

/**
 * The Challenge entity holds everything about a particular sales challenge:
 *   – its basic info (name, dates),
 *   – who participates,
 *   – the various scoring, winning & reward rules,
 *   – star-boost config,
 *   – status + soft-delete flag.
 *
 * We exclude the child collections from toString() to avoid recursive loops
 * when you accidentally log or print() a Challenge.
 */
@Entity
@Table(name = "challenges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {"rules", "winningRules", "rewardRules", "participants", "scoreRules"})
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    LocalDate startDate;

    @Column(nullable = false)
    LocalDate endDate;

    // 2) Who can participate
    @ElementCollection(targetClass = Role.class)
    @CollectionTable(
            name        = "challenge_target_roles",
            joinColumns = @JoinColumn(name = "challenge_id")
    )
    @Enumerated(EnumType.STRING)
    @Builder.Default
    Set<Role> targetRoles = new HashSet<>();

    // 3) Which products
    @ElementCollection(targetClass = Product.class)
    @CollectionTable(
            name        = "challenge_products",
            joinColumns = @JoinColumn(name = "challenge_id")
    )
    @Enumerated(EnumType.STRING)
    @Builder.Default
    Set<Product> targetProducts = new HashSet<>();

    // 4) Score rules (filters)
    @OneToMany(
            mappedBy      = "challenge",
            cascade       = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    List<Rule> rules = new ArrayList<>();

    // 5) Winning rules (threshold‐gates per role)
    @OneToMany(
            mappedBy      = "challenge",
            cascade       = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    List<ChallengeWinningRule> winningRules = new ArrayList<>();

    // 6) Reward rules (payout‐tiers per role)
    @OneToMany(
            mappedBy      = "challenge",
            cascade       = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    List<ChallengeRewardRule> rewardRules = new ArrayList<>();

    // 7) Participants (cascade‐delete when challenge is removed)
    @OneToMany(
            mappedBy      = "challenge",
            cascade       = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    List<ChallengeParticipant> participants = new ArrayList<>();

    // ─── NEW: per‐challenge score‐rules ─────────────────────────────────────────
    @OneToMany(
            mappedBy      = "challenge",
            cascade       = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    List<ScoreRule> scoreRules = new ArrayList<>();

    // 8) Status + soft‐delete
    @Enumerated(EnumType.STRING)
    @Builder.Default
    ChallengeStatus status = ChallengeStatus.ONGOING;

    @Builder.Default
    boolean deleted = false;
}
