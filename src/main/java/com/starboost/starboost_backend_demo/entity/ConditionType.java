package com.starboost.starboost_backend_demo.entity;

public enum ConditionType {
    MIN_CONTRACTS,
    MIN_REVENUE,
    MIN_AVG_PER_COMMERCIAL,    // flat average gate over agency
    MIN_AVG_PER_PV,            // flat average gate over region
    WEIGHTED_AVG_AGENCY,       // weighted average of agency‐score
    WEIGHTED_AVG_REGION        // weighted average of region‐score
}
