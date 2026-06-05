package com.edusphere.curriculum.repository;

import com.edusphere.curriculum.entity.LearningPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LearningPlanRepository extends JpaRepository<LearningPlan, UUID> {
    List<LearningPlan> findByStudentId(UUID studentId);
}
