package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_transaction")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SalesTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double premium;
    @Enumerated(EnumType.STRING) private Product product;
    @Enumerated(EnumType.STRING) private ContractType contractType;
    @Enumerated(EnumType.STRING) private TransactionNature transactionNature;
    private Long sellerId;
    @Enumerated(EnumType.STRING) private Role sellerRole;
    private Long agencyId;
    private Long regionId;
    private LocalDateTime saleDate;
    private String sellerName;

    /** ← NEW: which challenge this sale belongs to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;
}