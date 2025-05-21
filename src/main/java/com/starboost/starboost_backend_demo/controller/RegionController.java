// src/main/java/com/starboost/starboost_backend_demo/controller/RegionController.java
package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.RegionDto;
import com.starboost.starboost_backend_demo.service.RegionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {
    private final RegionService regionService;

    @GetMapping
    public List<RegionDto> getAll() {
        return regionService.findAll();
    }

    @GetMapping("/{id}")
    public RegionDto getOne(@PathVariable Long id) {
        return regionService.findById(id);
    }

    @PostMapping
    public RegionDto create(@Valid @RequestBody RegionDto dto) {
        return regionService.create(dto);
    }

    @PutMapping("/{id}")
    public RegionDto update(@PathVariable Long id,
                            @Valid @RequestBody RegionDto dto) {
        return regionService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        regionService.delete(id);
    }
}
