// src/main/java/com/starboost/starboost_backend_demo/dto/LoginRequest.java
package com.starboost.starboost_backend_demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for authentication requests.
 */
@Data
public class LoginRequest {
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}