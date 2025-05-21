// src/main/java/com/starboost/starboost_backend_demo/dto/admin/ParticipantDto.java
package com.starboost.starboost_backend_demo.dto.admin;

import lombok.Data;

/** A simplified version of ChallengeParticipantDto for admin metrics */
@Data
public class ParticipantDto {
    private Long participantId;
    private Long userId;
    private String role;
}
