// src/main/java/com/starboost/starboost_backend_demo/controller/admin/RegionAdminController.java
package com.starboost.starboost_backend_demo.controller.admin;

import com.starboost.starboost_backend_demo.dto.RegionDto;
import com.starboost.starboost_backend_demo.service.RegionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/regions")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class RegionAdminController {
    private final RegionService regionService;

    @GetMapping
    public List<RegionDto> listAll() {
        return regionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(regionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RegionDto> create(
            @Valid @RequestBody RegionDto dto
    ) {
        RegionDto created = regionService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDto> update(
            @PathVariable Long id,
            @Valid @RequestBody RegionDto dto
    ) {
        return ResponseEntity.ok(regionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        regionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
