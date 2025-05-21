package com.starboost.starboost_backend_demo.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String gender;       // M, F
    private LocalDate dateOfBirth;
    private String role;         // e.g. "AGENT"
    private String registrationNumber;
    private Long agencyId;       // null for roles that don't use agency
    private Long regionId;       // only for REGIONAL_MANAGER & ANIMATOR
    private String password;
}
