// src/main/java/com/starboost/starboost_backend_demo/dto/admin/ChallengeDto.java
package com.starboost.starboost_backend_demo.dto.admin;

import com.starboost.starboost_backend_demo.dto.RuleDto;
import com.starboost.starboost_backend_demo.dto.RewardRuleDto;
import com.starboost.starboost_backend_demo.dto.WinningRuleDto;
import com.starboost.starboost_backend_demo.entity.ChallengeStatus;
import com.starboost.starboost_backend_demo.entity.Product;
import com.starboost.starboost_backend_demo.entity.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/** For both admin create/update and response */
@Data
public class ChallengeDto {
    private Long id;

    @NotBlank private String name;
    @NotNull private LocalDate startDate;
    @NotNull private LocalDate endDate;

    @NotEmpty private Set<@NotNull Role> targetRoles;
    @NotEmpty private Set<@NotNull Product> targetProducts;

    @NotEmpty @Valid private List<RuleDto> rules;
    @NotEmpty @Valid private List<WinningRuleDto> winningRules;
    @NotEmpty @Valid private List<RewardRuleDto> rewardRules;

    @NotNull private ChallengeStatus status;
    private boolean deleted;
}
