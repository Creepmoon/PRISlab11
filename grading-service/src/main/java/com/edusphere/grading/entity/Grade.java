package com.edusphere.grading.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "grades")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID assignmentId;
    private UUID studentId;
    private UUID teacherId;
    private String subject;

    private int score;
    private int maxScore;
    private String feedback;
    private Instant gradedAt = Instant.now();
}
