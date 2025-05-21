package com.starboost.starboost_backend_demo.dto;

import com.starboost.starboost_backend_demo.entity.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Data transfer object for a Challenge definition,
 * including its score‐rules, winning‐rules, and reward‐rules.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeDto {
    private Long id;

    /** 1) Basic info */
    private String   name;
    private LocalDate startDate;
    private LocalDate endDate;

    /** 2) Roles eligible */
    private Set<Role> targetRoles;

    /** 3) Products counted */
    private Set<Product> targetProducts;

    /** 4) Score‐rule definitions (contract/pack/transaction filters) */
    private List<RuleDto> rules;

    /** 5) Winning‐rule (eligibility) gates per role */
    private List<WinningRuleDto> winningRules;

    /** 6) Reward‐rule (payout tiers) per role */
    private List<RewardRuleDto> rewardRules;

    /** 7) Star‐boost configuration */
    private StarBoostType starBoostType;
    private String        starBoostValue;

    /** 8) Status & soft‐delete flag */
    private ChallengeStatus status;
    private boolean          deleted;
}
