package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

/**
 * The Challenge entity holds everything about a particular sales challenge:
 *   – its basic info (name, dates),
 *   – who participates,
 *   – the various scoring, winning & reward rules,
 *   – star-boost config,
 *   – status + soft-delete flag.
 */
@Entity
@Table(name = "challenges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Challenge {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String   name;
    @Column(nullable = false) private LocalDate startDate;
    @Column(nullable = false) private LocalDate endDate;

    // 2) Who can participate
    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "challenge_target_roles",
            joinColumns = @JoinColumn(name = "challenge_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Role> targetRoles = new HashSet<>();

    // 3) Which products
    @ElementCollection(targetClass = Product.class)
    @CollectionTable(name = "challenge_products",
            joinColumns = @JoinColumn(name = "challenge_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Product> targetProducts = new HashSet<>();

    // 4) Score rules (filters)
    @OneToMany(mappedBy = "challenge",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<Rule> rules = new ArrayList<>();

    // 5) Winning rules (threshold‐gates per role)
    @OneToMany(mappedBy = "challenge",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<ChallengeWinningRule> winningRules = new ArrayList<>();

    // 6) Reward rules (payout‐tiers per role)
    @OneToMany(mappedBy = "challenge",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<ChallengeRewardRule> rewardRules = new ArrayList<>();

    // 7) Star-boost
    @Enumerated(EnumType.STRING)
    private StarBoostType starBoostType;
    private String        starBoostValue;

    // 8) Status + soft-delete
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ChallengeStatus status = ChallengeStatus.ONGOING;
    @Builder.Default
    private boolean deleted = false;
}
