// src/main/java/com/starboost/starboost_backend_demo/dto/user/ProfileUpdateDto.java
package com.starboost.starboost_backend_demo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileUpdateDto {
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String password;
    private String confirmPassword;

}
