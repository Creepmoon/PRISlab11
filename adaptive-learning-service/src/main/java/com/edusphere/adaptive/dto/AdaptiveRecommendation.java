package com.edusphere.adaptive.dto;

import com.edusphere.common.enums.ContentType;
import com.edusphere.common.enums.DifficultyLevel;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AdaptiveRecommendation {
    private UUID materialId;
    private String subject;
    private ContentType recommendedContentType;
    private DifficultyLevel recommendedDifficulty;
    private double confidence;
    private String reason;
}
