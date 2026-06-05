package com.edusphere.adaptive.entity;

import com.edusphere.common.enums.ContentType;
import com.edusphere.common.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "learning_events")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LearningEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID studentId;
    private UUID materialId;
    private String subject;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty;

    private double score;
    private long timeSpentSeconds;
    private boolean completed;
    private Instant recordedAt = Instant.now();
}
