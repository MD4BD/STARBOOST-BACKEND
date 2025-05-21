package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.RegionDto;
import com.starboost.starboost_backend_demo.entity.Region;
import com.starboost.starboost_backend_demo.repository.RegionRepository;
import com.starboost.starboost_backend_demo.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;

    @Override
    public List<RegionDto> findAll() {
        return regionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RegionDto findById(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region not found"));
        return toDto(region);
    }

    @Override
    public RegionDto create(RegionDto dto) {
        Region region = toEntity(dto);
        region = regionRepository.save(region);
        return toDto(region);
    }

    @Override
    public RegionDto update(Long id, RegionDto dto) {
        Region existing = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region not found"));
        existing.setCode(dto.getCode());
        existing.setName(dto.getName());
        existing = regionRepository.save(existing);
        return toDto(existing);
    }

    @Override
    public void delete(Long id) {
        regionRepository.deleteById(id);
    }

    private RegionDto toDto(Region region) {
        return RegionDto.builder()
                .id(region.getId())
                .code(region.getCode())
                .name(region.getName())
                .build();
    }

    private Region toEntity(RegionDto dto) {
        return Region.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .build();
    }
}