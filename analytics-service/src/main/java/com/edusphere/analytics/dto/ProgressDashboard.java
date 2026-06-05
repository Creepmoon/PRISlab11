package com.edusphere.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class ProgressDashboard {
    private UUID studentId;
    private double overallAverage;
    private double engagementScore;
    private int completedModules;
    private int totalModules;
    private Map<String, Double> subjectAverages;
    private List<SkillProgress> skills;
    private List<WeeklyActivity> weeklyActivity;
}
