package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents an enrollment of a User into a specific Challenge,
 * with no performance metrics here, only basic roster info and status.
 */
@Entity
@Table(name = "challenge_participants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private Long agencyId;
    private Long regionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ParticipantStatus status = ParticipantStatus.ACTIVE;
}
