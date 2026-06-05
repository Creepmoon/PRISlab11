package com.edusphere.ar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ar_class_sessions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ArClassSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String subject;
    private UUID teacherId;

    /** URL 3D-модели для AR (glTF/USDZ) */
    private String arModelUrl;

    private String status;
    private Instant scheduledAt;
    private Instant startedAt;
    private Instant endedAt;

    private double avgFps;
    private double avgLatencyMs;
}
