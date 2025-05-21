package com.starboost.starboost_backend_demo.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "challenge_rules")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** any one (or more) of these may be null to indicate “don’t care” */
    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @Enumerated(EnumType.STRING)
    private TransactionNature transactionNature;

    @Enumerated(EnumType.STRING)
    private PackType packType;

    /** link back to the parent Challenge */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;
}
