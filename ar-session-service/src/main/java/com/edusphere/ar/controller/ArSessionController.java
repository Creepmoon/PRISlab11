package com.edusphere.ar.controller;

import com.edusphere.ar.dto.ArSessionDto;
import com.edusphere.ar.service.ArSessionService;
import com.edusphere.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ar")
@RequiredArgsConstructor
public class ArSessionController {

    private final ArSessionService service;

    @PostMapping("/sessions")
    public ApiResponse<ArSessionDto> create(@RequestBody ArSessionDto dto) {
        return ApiResponse.ok(service.create(dto));
    }

    @GetMapping("/sessions/{id}")
    public ApiResponse<ArSessionDto> get(@PathVariable UUID id) {
        return ApiResponse.ok(service.getById(id));
    }

    @GetMapping("/sessions/active")
    public ApiResponse<List<ArSessionDto>> getActive() {
        return ApiResponse.ok(service.getActive());
    }

    @PostMapping("/sessions/{id}/start")
    public ApiResponse<ArSessionDto> start(@PathVariable UUID id) {
        return ApiResponse.ok(service.startSession(id));
    }

    @PostMapping("/sessions/{id}/metrics")
    public ApiResponse<ArSessionDto> metrics(
            @PathVariable UUID id,
            @RequestParam double fps,
            @RequestParam double latencyMs) {
        return ApiResponse.ok(service.reportMetrics(id, fps, latencyMs));
    }
}
