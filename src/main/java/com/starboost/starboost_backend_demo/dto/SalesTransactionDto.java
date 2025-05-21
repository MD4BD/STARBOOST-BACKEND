// src/main/java/com/starboost/starboost_backend_demo/dto/SalesTransactionDto.java
package com.starboost.starboost_backend_demo.dto;

import com.starboost.starboost_backend_demo.entity.*;
import lombok.*;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SalesTransactionDto {
    private Long          id;
    private Double        premium;
    private Product       product;
    private ContractType  contractType;
    private TransactionNature transactionNature;
    private Long          sellerId;
    private Role          sellerRole;
    private Long          agencyId;
    private Long          regionId;
    private LocalDateTime saleDate;
    private String        sellerName;
    private Long          challengeId;   // ← NEW
}