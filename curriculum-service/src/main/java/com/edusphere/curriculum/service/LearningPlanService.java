package com.edusphere.curriculum.service;

import com.edusphere.common.exception.ResourceNotFoundException;
import com.edusphere.curriculum.dto.GeneratePlanRequest;
import com.edusphere.curriculum.dto.LearningPlanDto;
import com.edusphere.curriculum.dto.PlanModule;
import com.edusphere.curriculum.entity.LearningPlan;
import com.edusphere.curriculum.repository.LearningPlanRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LearningPlanService {

    private final LearningPlanRepository repository;
    private final AiPlanGenerator aiPlanGenerator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public LearningPlanDto generatePlan(GeneratePlanRequest request) {
        LearningPlanDto generated = aiPlanGenerator.generate(request);
        return save(generated);
    }

    @Transactional
    public LearningPlanDto save(LearningPlanDto dto) {
        LearningPlan plan = dto.getId() != null
                ? repository.findById(dto.getId()).orElse(new LearningPlan())
                : new LearningPlan();

        plan.setStudentId(dto.getStudentId());
        plan.setTeacherId(dto.getTeacherId());
        plan.setTitle(dto.getTitle());
        plan.setGoals(dto.getGoals());
        plan.setModulesJson(serializeModules(dto.getModules()));
        plan.setTargetLevel(dto.getTargetLevel());
        plan.setStatus(dto.getStatus() != null ? dto.getStatus() : "DRAFT");
        plan.setAiConfidenceScore(dto.getAiConfidenceScore());
        plan.setUpdatedAt(Instant.now());

        return toDto(repository.save(plan));
    }

    @Transactional
    public LearningPlanDto approveByTeacher(UUID planId, UUID teacherId, LearningPlanDto updates) {
        LearningPlan plan = repository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));
        plan.setTeacherId(teacherId);
        plan.setStatus("TEACHER_APPROVED");
        if (updates.getModules() != null) {
            plan.setModulesJson(serializeModules(updates.getModules()));
        }
        if (updates.getTitle() != null) plan.setTitle(updates.getTitle());
        plan.setUpdatedAt(Instant.now());
        return toDto(repository.save(plan));
    }

    public LearningPlanDto getById(UUID id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));
    }

    public List<LearningPlanDto> getByStudent(UUID studentId) {
        return repository.findByStudentId(studentId).stream().map(this::toDto).toList();
    }

    private String serializeModules(List<PlanModule> modules) {
        try {
            return objectMapper.writeValueAsString(modules);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize modules", e);
        }
    }

    private List<PlanModule> deserializeModules(String json) {
        if (json == null) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return List.of();
        }
    }

    private LearningPlanDto toDto(LearningPlan p) {
        return LearningPlanDto.builder()
                .id(p.getId())
                .studentId(p.getStudentId())
                .teacherId(p.getTeacherId())
                .title(p.getTitle())
                .goals(p.getGoals())
                .modules(deserializeModules(p.getModulesJson()))
                .targetLevel(p.getTargetLevel())
                .status(p.getStatus())
                .aiConfidenceScore(p.getAiConfidenceScore())
                .build();
    }
}
