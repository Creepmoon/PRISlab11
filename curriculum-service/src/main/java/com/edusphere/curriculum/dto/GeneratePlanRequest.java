package com.edusphere.curriculum.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GeneratePlanRequest {
    private UUID studentId;
    private String goals;
    private List<String> interests;
    private List<String> subjects;
    private int weeklyHours;
}
