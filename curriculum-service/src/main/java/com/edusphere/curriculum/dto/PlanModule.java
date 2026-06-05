package com.edusphere.curriculum.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PlanModule {
    private String name;
    private String subject;
    private int orderIndex;
    private UUID materialId;
    private int estimatedHours;
}
