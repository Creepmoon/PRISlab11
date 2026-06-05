package com.edusphere.ar.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ArSessionDto {
    private UUID id;
    private String title;
    private String subject;
    private UUID teacherId;
    private String arModelUrl;
    private String status;
    private Instant scheduledAt;
    private double avgFps;
    private double avgLatencyMs;
    private boolean arSupported;
}
