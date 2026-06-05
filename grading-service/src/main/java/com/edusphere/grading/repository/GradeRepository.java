package com.edusphere.grading.repository;

import com.edusphere.grading.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GradeRepository extends JpaRepository<Grade, UUID> {
    List<Grade> findByStudentIdOrderByGradedAtDesc(UUID studentId);
    List<Grade> findByStudentIdAndSubject(UUID studentId, String subject);
}
