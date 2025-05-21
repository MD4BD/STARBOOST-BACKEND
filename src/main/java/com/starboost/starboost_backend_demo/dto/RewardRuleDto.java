package com.starboost.starboost_backend_demo.dto;

import lombok.*;
import com.starboost.starboost_backend_demo.entity.PayoutType;
import com.starboost.starboost_backend_demo.entity.Role;

/**

 Exposes the payout tiers to the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardRuleDto {
    private Long id;
    private Role roleCategory;
    private PayoutType payoutType;
    private Double tierMin;
    private Double tierMax;
    private Double baseAmount;
}