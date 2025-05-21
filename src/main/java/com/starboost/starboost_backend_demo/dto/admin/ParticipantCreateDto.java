// src/main/java/com/starboost/starboost_backend_demo/dto/admin/ParticipantCreateDto.java
package com.starboost.starboost_backend_demo.dto.admin;

import com.starboost.starboost_backend_demo.entity.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

/**
 * POST body for enrolling participants in a challenge.
 */
@Data
public class ParticipantCreateDto {
    @NotEmpty(message = "At least one user ID is required")
    private Set<@NotNull Long> userIds;

    @NotNull(message = "Role is required")
    private Role role;
}
