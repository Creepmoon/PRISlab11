package com.edusphere.grading.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assignments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String subject;
    private UUID teacherId;
    private UUID materialId;
    private Instant dueDate;
    private Instant createdAt = Instant.now();
}
