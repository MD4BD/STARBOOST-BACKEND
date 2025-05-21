package com.starboost.starboost_backend_demo.dto;

import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.entity.ParticipantStatus;
import lombok.*;

/**
 * Basic challenge enrollment info (roster) without performance metrics.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeParticipantDto {
    private Long participantId;
    private Long challengeId;
    private Long userId;
    private String firstName;
    private String lastName;
    private Role role;
    private Long agencyId;
    private Long regionId;
    private ParticipantStatus status;  // ACTIVE or INACTIVE
}