package com.starboost.starboost_backend_demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgencyDto {
    private Long id;
    private String code;
    private String name;
    private Long regionId;
}