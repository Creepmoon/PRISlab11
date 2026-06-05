package com.edusphere.grading.controller;

import com.edusphere.common.dto.ApiResponse;
import com.edusphere.grading.entity.Assignment;
import com.edusphere.grading.entity.Grade;
import com.edusphere.grading.service.GradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/grading")
@RequiredArgsConstructor
public class GradingController {

    private final GradingService service;

    @PostMapping("/assignments")
    public ApiResponse<Assignment> createAssignment(@RequestBody Assignment assignment) {
        return ApiResponse.ok(service.createAssignment(assignment));
    }

    @PostMapping("/grades")
    public ApiResponse<Grade> submitGrade(@RequestBody Grade grade) {
        return ApiResponse.ok("Grade submitted", service.submitGrade(grade));
    }

    @GetMapping("/grades/student/{studentId}")
    public ApiResponse<List<Grade>> getGrades(
            @PathVariable UUID studentId,
            @RequestParam(required = false) String subject) {
        if (subject != null) {
            return ApiResponse.ok(service.getStudentGradesBySubject(studentId, subject));
        }
        return ApiResponse.ok(service.getStudentGrades(studentId));
    }
}
