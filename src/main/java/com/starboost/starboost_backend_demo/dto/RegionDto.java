// src/main/java/com/starboost/starboost_backend_demo/dto/RegionDto.java
package com.starboost.starboost_backend_demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for Region create/update operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionDto {
    private Long id;

    @NotBlank(message = "Region code is required")
    private String code;

    @NotBlank(message = "Region name is required")
    private String name;
}
