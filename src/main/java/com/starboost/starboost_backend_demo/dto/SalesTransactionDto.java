// src/main/java/com/starboost/starboost_backend_demo/dto/SalesTransactionDto.java
package com.starboost.starboost_backend_demo.dto;

import com.starboost.starboost_backend_demo.entity.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for SalesTransaction create operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesTransactionDto {
    private Long id;

    @NotNull(message = "Premium is required")
    @Min(value = 0, message = "Premium must be non‐negative")
    private Double premium;

    @NotNull(message = "Product is required")
    private Product product;

    @NotNull(message = "Contract type is required")
    private ContractType contractType;

    @NotNull(message = "Transaction nature is required")
    private TransactionNature transactionNature;

    @NotNull(message = "pack Type is required")
    private PackType packType;

    @NotNull(message = "Seller ID is required")
    private Long sellerId;

    @NotNull(message = "Seller role is required")
    private Role sellerRole;

    private Long agencyId;
    private Long regionId;

    @NotNull(message = "Sale date is required")
    private LocalDateTime saleDate;

    @NotBlank(message = "Seller name is required")
    private String sellerName;

    private Long challengeId;
}
