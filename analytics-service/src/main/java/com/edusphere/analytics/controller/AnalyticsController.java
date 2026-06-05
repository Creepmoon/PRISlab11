package com.edusphere.analytics.controller;

import com.edusphere.analytics.dto.ProgressDashboard;
import com.edusphere.analytics.service.AnalyticsAggregator;
import com.edusphere.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsAggregator aggregator;

    @GetMapping("/dashboard/student/{studentId}")
    public ApiResponse<ProgressDashboard> studentDashboard(@PathVariable UUID studentId) {
        return ApiResponse.ok(aggregator.getStudentDashboard(studentId));
    }
}
