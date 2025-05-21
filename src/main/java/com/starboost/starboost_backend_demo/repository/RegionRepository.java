package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}

