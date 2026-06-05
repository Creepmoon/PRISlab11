package com.edusphere.materials.controller;

import com.edusphere.common.dto.ApiResponse;
import com.edusphere.materials.dto.MaterialDto;
import com.edusphere.materials.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService service;

    @PostMapping
    public ApiResponse<MaterialDto> create(@RequestBody MaterialDto dto) {
        return ApiResponse.ok("Material created", service.create(dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<MaterialDto> getById(@PathVariable UUID id) {
        return ApiResponse.ok(service.getById(id));
    }

    @GetMapping
    public ApiResponse<List<MaterialDto>> getAll(@RequestParam(required = false) String subject) {
        if (subject != null) {
            return ApiResponse.ok(service.getBySubject(subject));
        }
        return ApiResponse.ok(service.getAll());
    }
}
