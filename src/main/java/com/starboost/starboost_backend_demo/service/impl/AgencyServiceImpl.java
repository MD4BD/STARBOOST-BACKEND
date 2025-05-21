package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.AgencyDto;
import com.starboost.starboost_backend_demo.entity.Agency;
import com.starboost.starboost_backend_demo.entity.Region;
import com.starboost.starboost_backend_demo.repository.AgencyRepository;
import com.starboost.starboost_backend_demo.repository.RegionRepository;
import com.starboost.starboost_backend_demo.service.AgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgencyServiceImpl implements AgencyService {
    private final AgencyRepository agencyRepository;
    private final RegionRepository regionRepository;

    @Override
    public List<AgencyDto> findAll() {
        return agencyRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AgencyDto findById(Long id) {
        Agency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agency not found"));
        return toDto(agency);
    }

    @Override
    public AgencyDto create(AgencyDto dto) {
        Agency agency = toEntity(dto);
        agency = agencyRepository.save(agency);
        return toDto(agency);
    }

    @Override
    public AgencyDto update(Long id, AgencyDto dto) {
        Agency existing = agencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agency not found"));
        existing.setCode(dto.getCode());
        existing.setName(dto.getName());
        existing.setRegion(regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found")));
        existing = agencyRepository.save(existing);
        return toDto(existing);
    }

    @Override
    public void delete(Long id) {
        agencyRepository.deleteById(id);
    }

    private AgencyDto toDto(Agency agency) {
        return AgencyDto.builder()
                .id(agency.getId())
                .code(agency.getCode())
                .name(agency.getName())
                .regionId(agency.getRegion().getId())
                .build();
    }

    private Agency toEntity(AgencyDto dto) {
        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found"));
        return Agency.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .region(region)
                .build();
    }
}

