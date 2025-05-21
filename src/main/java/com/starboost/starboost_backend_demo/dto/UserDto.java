// src/main/java/com/starboost/starboost_backend_demo/dto/UserDto.java
package com.starboost.starboost_backend_demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO for User create/update operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Gender is required")
    private String gender;       // M, F

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Role is required")
    private String role;         // e.g. "AGENT"

    private String registrationNumber;

    private Long agencyId;       // null for roles that don't use agency

    private Long regionId;       // only for REGIONAL_MANAGER & ANIMATOR

    private String agencyName;

    private String regionName;

    private Boolean active;

    @NotBlank(message = "Password is required")
    private String password;
}
