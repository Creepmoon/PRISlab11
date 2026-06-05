package com.edusphere.analytics.service;

import com.edusphere.analytics.dto.ProgressDashboard;
import com.edusphere.analytics.dto.SkillProgress;
import com.edusphere.analytics.dto.WeeklyActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsAggregator {

    @Value("${edusphere.services.grading:http://localhost:8086}")
    private String gradingUrl;

    @Value("${edusphere.services.adaptive:http://localhost:8085}")
    private String adaptiveUrl;

    private final WebClient webClient = WebClient.create();

    public ProgressDashboard getStudentDashboard(UUID studentId) {
        List<Map<String, Object>> grades = fetchGrades(studentId);

        Map<String, Double> subjectAverages = new HashMap<>();
        double total = 0;
        int count = 0;

        for (Map<String, Object> g : grades) {
            String subject = (String) g.get("subject");
            int score = ((Number) g.get("score")).intValue();
            int maxScore = ((Number) g.get("maxScore")).intValue();
            double pct = maxScore > 0 ? (double) score / maxScore * 100 : 0;
            subjectAverages.merge(subject, pct, (a, b) -> (a + b) / 2);
            total += pct;
            count++;
        }

        double overall = count > 0 ? total / count : 0;

        return ProgressDashboard.builder()
                .studentId(studentId)
                .overallAverage(overall)
                .engagementScore(Math.min(100, 50 + count * 5))
                .completedModules(count)
                .totalModules(Math.max(count, 10))
                .subjectAverages(subjectAverages)
                .skills(buildSkills(subjectAverages))
                .weeklyActivity(buildWeeklyActivity(count))
                .build();
    }

    private List<Map<String, Object>> fetchGrades(UUID studentId) {
        try {
            var response = webClient.get()
                    .uri(gradingUrl + "/api/grading/grades/student/" + studentId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
            if (response != null && response.get("data") instanceof List<?> list) {
                return (List<Map<String, Object>>) list;
            }
        } catch (Exception ignored) {
        }
        return List.of();
    }

    private List<SkillProgress> buildSkills(Map<String, Double> subjectAverages) {
        return subjectAverages.entrySet().stream()
                .map(e -> SkillProgress.builder()
                        .skillName(e.getKey())
                        .level(e.getValue() / 100)
                        .growth(0.05 + Math.random() * 0.1)
                        .build())
                .toList();
    }

    private List<WeeklyActivity> buildWeeklyActivity(int base) {
        List<WeeklyActivity> activities = new ArrayList<>();
        for (int i = 4; i >= 1; i--) {
            activities.add(WeeklyActivity.builder()
                    .week("Week " + i)
                    .hoursSpent(base + i * 2)
                    .assignmentsCompleted(Math.max(0, base - i + 2))
                    .build());
        }
        return activities;
    }
}
