// src/main/java/com/starboost/starboost_backend_demo/dto/ScoreRuleDto.java
package com.starboost.starboost_backend_demo.dto;

import com.starboost.starboost_backend_demo.entity.ContractType;
import com.starboost.starboost_backend_demo.entity.PackType;
import com.starboost.starboost_backend_demo.entity.ScoreType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for ScoreRule create/update operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreRuleDto {
    private Long id;

    @NotNull(message = "Score type is required")
    private ScoreType scoreType;

    private ContractType contractType;

    private PackType packType;

    @NotNull(message = "Points is required")
    @Min(value = 0, message = "Points must be non-negative")
    private Integer points;

    // <— no @NotNull here, so null is allowed for CONTRACT/PACK rules
    @Min(value = 1, message = "Revenue unit must be at least 1")
    private Integer revenueUnit;

    private Long challengeId;
}
