package com.starboost.starboost_backend_demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChallengeLeaderboardDto {
    private int    rank;
    private Long   userId;       // for agent/commercial, or null for agency/region
    private String name;         // user full name or agency/region name
    private String role;         // AGENT/COMMERCIAL/AGENCY/REGION
    private Long   agencyId;     // for user entries or agency listings
    private String agencyName;
    private Long   regionId;     // for user entries or region listings
    private String regionName;
    private int    score;
}
