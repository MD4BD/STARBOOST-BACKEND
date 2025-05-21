// src/main/java/com/starboost/starboost_backend_demo/entity/Rule.java
package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * A “filter” rule for a Challenge: any of its fields may be null to mean “don’t care.”
 * We exclude the parent Challenge from toString() so that printing a Rule
 * won’t traverse back into Challenge → Rule → Challenge → … endlessly.
 */
@Entity
@Table(name = "challenge_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = "challenge")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /** any one (or more) of these may be null to indicate “don’t care” */
    @Enumerated(EnumType.STRING)
    ContractType contractType;

    @Enumerated(EnumType.STRING)
    TransactionNature transactionNature;

    @Enumerated(EnumType.STRING)
    PackType packType;

    /** link back to the parent Challenge (LAZY to avoid eager loading) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    Challenge challenge;
}
