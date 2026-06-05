package com.edusphere.ar.service;

import com.edusphere.ar.dto.ArSessionDto;
import com.edusphere.ar.entity.ArClassSession;
import com.edusphere.ar.repository.ArSessionRepository;
import com.edusphere.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArSessionService {

    private final ArSessionRepository repository;

    @Transactional
    public ArSessionDto create(ArSessionDto dto) {
        ArClassSession session = ArClassSession.builder()
                .title(dto.getTitle())
                .subject(dto.getSubject())
                .teacherId(dto.getTeacherId())
                .arModelUrl(dto.getArModelUrl())
                .status("SCHEDULED")
                .scheduledAt(dto.getScheduledAt())
                .build();
        return toDto(repository.save(session));
    }

    @Transactional
    public ArSessionDto startSession(UUID id) {
        ArClassSession session = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AR session not found"));
        session.setStatus("ACTIVE");
        session.setStartedAt(Instant.now());
        return toDto(repository.save(session));
    }

    @Transactional
    public ArSessionDto reportMetrics(UUID id, double fps, double latencyMs) {
        ArClassSession session = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AR session not found"));
        session.setAvgFps(fps);
        session.setAvgLatencyMs(latencyMs);
        return toDto(repository.save(session));
    }

    public ArSessionDto getById(UUID id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("AR session not found"));
    }

    public List<ArSessionDto> getActive() {
        return repository.findByStatus("ACTIVE").stream().map(this::toDto).toList();
    }

    private ArSessionDto toDto(ArClassSession s) {
        return ArSessionDto.builder()
                .id(s.getId())
                .title(s.getTitle())
                .subject(s.getSubject())
                .teacherId(s.getTeacherId())
                .arModelUrl(s.getArModelUrl())
                .status(s.getStatus())
                .scheduledAt(s.getScheduledAt())
                .avgFps(s.getAvgFps())
                .avgLatencyMs(s.getAvgLatencyMs())
                .arSupported(true)
                .build();
    }
}
