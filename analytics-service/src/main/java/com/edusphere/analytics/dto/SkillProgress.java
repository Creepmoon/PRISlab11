package com.edusphere.analytics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillProgress {
    private String skillName;
    private double level;
    private double growth;
}
