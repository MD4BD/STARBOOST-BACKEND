// src/main/java/com/starboost/starboost_backend_demo/dto/admin/PerformanceDto.java
package com.starboost.starboost_backend_demo.dto.admin;

import lombok.Data;

/** A simplified version of PerformanceDto for admin metrics */
@Data
public class PerformanceDto {
    private Long userId;
    private String name;
    private long totalContracts;
    private double totalRevenue;
    private int totalScore;
}
