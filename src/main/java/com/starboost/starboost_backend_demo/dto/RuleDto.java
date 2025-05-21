package com.starboost.starboost_backend_demo.dto;

import com.starboost.starboost_backend_demo.entity.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RuleDto {
    private Long id;
    private ContractType contractType;
    private TransactionNature transactionNature;
    private PackType packType;
}