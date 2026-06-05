package com.edusphere.curriculum.service;

import com.edusphere.common.enums.DifficultyLevel;
import com.edusphere.curriculum.dto.GeneratePlanRequest;
import com.edusphere.curriculum.dto.LearningPlanDto;
import com.edusphere.curriculum.dto.PlanModule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Ядро генерации учебных планов на основе эвристик и симуляции ИИ-рекомендаций.
 * В production интегрируется с LLM/ML-сервисом.
 */
@Component
public class AiPlanGenerator {

    public LearningPlanDto generate(GeneratePlanRequest request) {
        List<PlanModule> modules = new ArrayList<>();
        List<String> subjects = request.getSubjects() != null && !request.getSubjects().isEmpty()
                ? request.getSubjects()
                : List.of("Mathematics", "Physics", "Programming");

        int order = 1;
        int hoursPerSubject = Math.max(2, request.getWeeklyHours() / subjects.size());

        for (String subject : subjects) {
            modules.add(PlanModule.builder()
                    .name("Introduction to " + subject)
                    .subject(subject)
                    .orderIndex(order++)
                    .estimatedHours(hoursPerSubject)
                    .build());
            modules.add(PlanModule.builder()
                    .name("Advanced " + subject)
                    .subject(subject)
                    .orderIndex(order++)
                    .estimatedHours(hoursPerSubject + 2)
                    .build());
        }

        if (request.getInterests() != null) {
            for (String interest : request.getInterests()) {
                modules.add(PlanModule.builder()
                        .name("Elective: " + interest)
                        .subject(interest)
                        .orderIndex(order++)
                        .estimatedHours(3)
                        .build());
            }
        }

        double confidence = calculateConfidence(request, modules);

        return LearningPlanDto.builder()
                .studentId(request.getStudentId())
                .title("Personalized Learning Path")
                .goals(request.getGoals())
                .modules(modules)
                .targetLevel(DifficultyLevel.INTERMEDIATE)
                .status("AI_GENERATED")
                .aiConfidenceScore(confidence)
                .build();
    }

    private double calculateConfidence(GeneratePlanRequest request, List<PlanModule> modules) {
        double base = 0.75;
        if (request.getGoals() != null && !request.getGoals().isBlank()) base += 0.05;
        if (request.getInterests() != null && !request.getInterests().isEmpty()) base += 0.08;
        if (modules.size() >= 4) base += 0.05;
        return Math.min(0.97, base);
    }
}
