package com.edusphere.materials.dto;

import com.edusphere.common.enums.ContentType;
import com.edusphere.common.enums.DifficultyLevel;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MaterialDto {
    private UUID id;
    private String title;
    private String description;
    private String subject;
    private ContentType contentType;
    private DifficultyLevel difficulty;
    private String content;
    private UUID authorId;
    private boolean published;
}
