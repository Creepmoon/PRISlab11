package com.edusphere.curriculum.dto;

import com.edusphere.common.enums.DifficultyLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class LearningPlanDto {
    private UUID id;
    private UUID studentId;
    private UUID teacherId;
    private String title;
    private String goals;
    private List<PlanModule> modules;
    private DifficultyLevel targetLevel;
    private String status;
    private double aiConfidenceScore;
}
