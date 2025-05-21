// src/main/java/com/starboost/starboost_backend_demo/controller/admin/SalesAdminController.java
package com.starboost.starboost_backend_demo.controller.admin;

import com.starboost.starboost_backend_demo.dto.SalesTransactionDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.service.SalesTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/sales")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class SalesAdminController {

    private final SalesTransactionService service;

    @GetMapping
    public List<SalesTransactionDto> listAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesTransactionDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<SalesTransactionDto> create(
            @Valid @RequestBody SalesTransactionDto dto
    ) {
        SalesTransactionDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/admin/sales/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesTransactionDto> update(
            @PathVariable Long id,
            @Valid @RequestBody SalesTransactionDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
