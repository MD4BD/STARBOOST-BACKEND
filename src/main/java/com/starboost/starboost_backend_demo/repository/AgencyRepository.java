package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository extends JpaRepository<Agency, Long> {

    /** count how many agencies sit in a given region */
    long countByRegionId(Long regionId);

}
