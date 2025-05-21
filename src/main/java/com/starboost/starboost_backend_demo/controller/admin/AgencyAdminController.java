// src/main/java/com/starboost/starboost_backend_demo/controller/admin/AgencyAdminController.java
package com.starboost.starboost_backend_demo.controller.admin;

import com.starboost.starboost_backend_demo.dto.AgencyDto;
import com.starboost.starboost_backend_demo.service.AgencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/agencies")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AgencyAdminController {
    private final AgencyService agencyService;

    @GetMapping
    public List<AgencyDto> listAll() {
        return agencyService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgencyDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(agencyService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AgencyDto> create(
            @Valid @RequestBody AgencyDto dto
    ) {
        AgencyDto created = agencyService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgencyDto> update(
            @PathVariable Long id,
            @Valid @RequestBody AgencyDto dto
    ) {
        return ResponseEntity.ok(agencyService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agencyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
