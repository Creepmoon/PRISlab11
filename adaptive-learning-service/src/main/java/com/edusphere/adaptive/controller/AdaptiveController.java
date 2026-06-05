package com.edusphere.adaptive.controller;

import com.edusphere.adaptive.dto.AdaptiveRecommendation;
import com.edusphere.adaptive.entity.LearningEvent;
import com.edusphere.adaptive.service.AdaptiveEngine;
import com.edusphere.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/adaptive")
@RequiredArgsConstructor
public class AdaptiveController {

    private final AdaptiveEngine engine;

    @PostMapping("/events")
    public ApiResponse<LearningEvent> recordEvent(@RequestBody LearningEvent event) {
        return ApiResponse.ok(engine.recordEvent(event));
    }

    @GetMapping("/recommendations/{studentId}")
    public ApiResponse<List<AdaptiveRecommendation>> recommend(@PathVariable UUID studentId) {
        return ApiResponse.ok(engine.getRecommendations(studentId));
    }
}
