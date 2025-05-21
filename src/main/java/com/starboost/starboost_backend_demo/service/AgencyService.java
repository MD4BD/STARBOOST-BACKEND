package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.AgencyDto;
import java.util.List;

public interface AgencyService {
    List<AgencyDto> findAll();
    AgencyDto findById(Long id);
    AgencyDto create(AgencyDto agencyDto);
    AgencyDto update(Long id, AgencyDto agencyDto);
    void delete(Long id);
}