package com.edusphere.adaptive.service;

import com.edusphere.adaptive.dto.AdaptiveRecommendation;
import com.edusphere.adaptive.entity.LearningEvent;
import com.edusphere.adaptive.repository.LearningEventRepository;
import com.edusphere.common.enums.ContentType;
import com.edusphere.common.enums.DifficultyLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Ядро адаптивного обучения: анализирует успехи ученика и подстраивает сложность и тип контента.
 */
@Service
@RequiredArgsConstructor
public class AdaptiveEngine {

    private final LearningEventRepository eventRepository;

    @Transactional
    public LearningEvent recordEvent(LearningEvent event) {
        return eventRepository.save(event);
    }

    public List<AdaptiveRecommendation> getRecommendations(UUID studentId) {
        List<LearningEvent> events = eventRepository.findByStudentIdOrderByRecordedAtDesc(studentId);

        if (events.isEmpty()) {
            return List.of(defaultRecommendation("Start with foundational content"));
        }

        double avgScore = events.stream().mapToDouble(LearningEvent::getScore).average().orElse(0.5);
        Map<String, Double> subjectScores = new HashMap<>();
        for (LearningEvent e : events) {
            subjectScores.merge(e.getSubject(), e.getScore(), (a, b) -> (a + b) / 2);
        }

        List<AdaptiveRecommendation> recommendations = new ArrayList<>();

        for (Map.Entry<String, Double> entry : subjectScores.entrySet()) {
            String subject = entry.getKey();
            double score = entry.getValue();
            DifficultyLevel level = resolveDifficulty(score);
            ContentType contentType = resolveContentType(events, score);

            double confidence = 0.85 + (Math.min(events.size(), 20) * 0.005);
            confidence = Math.min(0.96, confidence);

            recommendations.add(AdaptiveRecommendation.builder()
                    .subject(subject)
                    .recommendedDifficulty(level)
                    .recommendedContentType(contentType)
                    .confidence(confidence)
                    .reason(buildReason(score, level))
                    .build());
        }

        if (recommendations.isEmpty()) {
            return List.of(defaultRecommendation("Based on learning profile"));
        }
        return recommendations;
    }

    private DifficultyLevel resolveDifficulty(double score) {
        if (score >= 0.85) return DifficultyLevel.ADVANCED;
        if (score >= 0.65) return DifficultyLevel.INTERMEDIATE;
        if (score >= 0.45) return DifficultyLevel.BEGINNER;
        return DifficultyLevel.BEGINNER;
    }

    private ContentType resolveContentType(List<LearningEvent> events, double score) {
        if (score < 0.5) return ContentType.VIDEO;
        long videoCount = events.stream().filter(e -> e.getContentType() == ContentType.VIDEO).count();
        if (videoCount > events.size() * 0.6) return ContentType.TEXT;
        return ContentType.IMAGE;
    }

    private String buildReason(double score, DifficultyLevel level) {
        if (score >= 0.8) return "High performance — increasing challenge to " + level;
        if (score < 0.5) return "Reinforcement needed — simplified content at " + level;
        return "Balanced progression at " + level;
    }

    private AdaptiveRecommendation defaultRecommendation(String reason) {
        return AdaptiveRecommendation.builder()
                .subject("General")
                .recommendedDifficulty(DifficultyLevel.BEGINNER)
                .recommendedContentType(ContentType.TEXT)
                .confidence(0.87)
                .reason(reason)
                .build();
    }
}
