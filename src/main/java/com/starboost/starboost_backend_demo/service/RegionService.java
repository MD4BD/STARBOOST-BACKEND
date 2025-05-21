package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.RegionDto;
import java.util.List;

public interface RegionService {
    List<RegionDto> findAll();
    RegionDto findById(Long id);
    RegionDto create(RegionDto regionDto);
    RegionDto update(Long id, RegionDto regionDto);
    void delete(Long id);
}