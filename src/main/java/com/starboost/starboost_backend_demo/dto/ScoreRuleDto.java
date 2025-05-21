package com.starboost.starboost_backend_demo.dto;

import com.starboost.starboost_backend_demo.entity.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreRuleDto {
    private Long        id;
    private ScoreType   scoreType;
    private ContractType contractType;
    private PackType     packType;
    private Integer      points;
    private Integer      revenueUnit;
    private Long         challengeId;   // ← NEW
}