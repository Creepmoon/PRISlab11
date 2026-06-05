package com.edusphere.curriculum.entity;

import com.edusphere.common.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "learning_plans")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LearningPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID studentId;
    private UUID teacherId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String goals;

    @Column(columnDefinition = "TEXT")
    private String modulesJson;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel targetLevel;

    /** AI-generated, teacher-approved, draft */
    private String status;

    private double aiConfidenceScore;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
}
