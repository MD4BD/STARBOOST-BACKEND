// src/main/java/com/starboost/starboost_backend_demo/controller/AgencyController.java
package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.AgencyDto;
import com.starboost.starboost_backend_demo.service.AgencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agencies")
@RequiredArgsConstructor
public class AgencyController {
    private final AgencyService agencyService;

    @GetMapping
    public List<AgencyDto> getAll() {
        return agencyService.findAll();
    }

    @GetMapping("/{id}")
    public AgencyDto getOne(@PathVariable Long id) {
        return agencyService.findById(id);
    }

    @PostMapping
    public AgencyDto create(@Valid @RequestBody AgencyDto dto) {
        return agencyService.create(dto);
    }

    @PutMapping("/{id}")
    public AgencyDto update(@PathVariable Long id,
                            @Valid @RequestBody AgencyDto dto) {
        return agencyService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        agencyService.delete(id);
    }
}
