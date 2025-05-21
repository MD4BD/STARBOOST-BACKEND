package com.starboost.starboost_backend_demo.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "score_rules")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ScoreRule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ScoreType scoreType;

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @Enumerated(EnumType.STRING)
    private PackType packType;

    private Integer points;
    private Integer revenueUnit;

    /** ← NEW: link each rule to exactly one Challenge */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;
}