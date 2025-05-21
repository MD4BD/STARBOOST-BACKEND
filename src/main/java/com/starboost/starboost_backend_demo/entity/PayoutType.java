package com.starboost.starboost_backend_demo.entity;

public enum PayoutType {
    /** a fixed gift or amount for anyone whose metric falls in [tierMin, tierMax] */
    FIXED_TIERS,

    /** pay X% of the user’s raw metric (e.g. revenue) */
    PERCENT_TIERS,

    /** pay a fixed amount based on the user’s FINAL rank (1, 2, 3…) */
    RANK_TIERS
}
