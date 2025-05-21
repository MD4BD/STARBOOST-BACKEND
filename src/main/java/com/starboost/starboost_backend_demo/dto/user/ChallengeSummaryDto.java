// src/main/java/com/starboost/starboost_backend_demo/dto/user/ChallengeSummaryDto.java
package com.starboost.starboost_backend_demo.dto.user;

import com.starboost.starboost_backend_demo.entity.ChallengeStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ChallengeSummaryDto {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private ChallengeStatus status;
}
