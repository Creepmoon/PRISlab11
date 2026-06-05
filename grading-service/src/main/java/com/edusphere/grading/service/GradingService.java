package com.edusphere.grading.service;

import com.edusphere.grading.entity.Assignment;
import com.edusphere.grading.entity.Grade;
import com.edusphere.grading.repository.AssignmentRepository;
import com.edusphere.grading.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GradingService {

    private final GradeRepository gradeRepository;
    private final AssignmentRepository assignmentRepository;

    @Transactional
    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Transactional
    public Grade submitGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public List<Grade> getStudentGrades(UUID studentId) {
        return gradeRepository.findByStudentIdOrderByGradedAtDesc(studentId);
    }

    public List<Grade> getStudentGradesBySubject(UUID studentId, String subject) {
        return gradeRepository.findByStudentIdAndSubject(studentId, subject);
    }
}
