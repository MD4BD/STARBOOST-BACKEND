// src/main/java/com/starboost/starboost_backend_demo/dto/admin/UserUpdateDto.java
package com.starboost.starboost_backend_demo.dto.admin;

import com.starboost.starboost_backend_demo.dto.UserDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDto {
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @Email @NotBlank private String email;
    @NotBlank private String phoneNumber;
    @NotNull private LocalDate dateOfBirth;  // <-- changed
    @NotBlank private String gender;
    private String password;
    @NotBlank private String role;
    private Long agencyId;
    private Long regionId;
    @NotNull private Boolean active;

    public UserDto toUserDto() {
        UserDto u = new UserDto();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setPhoneNumber(phoneNumber);
        u.setDateOfBirth(dateOfBirth);
        u.setGender(gender);
        u.setPassword(password);
        u.setRole(role);
        u.setAgencyId(agencyId);
        u.setRegionId(regionId);
        u.setActive(active);
        return u;
    }
}
