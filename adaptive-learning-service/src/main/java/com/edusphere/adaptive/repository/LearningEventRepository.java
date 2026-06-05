package com.edusphere.adaptive.repository;

import com.edusphere.adaptive.entity.LearningEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LearningEventRepository extends JpaRepository<LearningEvent, UUID> {
    List<LearningEvent> findByStudentIdOrderByRecordedAtDesc(UUID studentId);
}
