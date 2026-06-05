package com.edusphere.materials.repository;

import com.edusphere.materials.entity.EducationalMaterial;
import com.edusphere.common.enums.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MaterialRepository extends JpaRepository<EducationalMaterial, UUID> {
    List<EducationalMaterial> findBySubjectAndPublishedTrue(String subject);
    List<EducationalMaterial> findByContentTypeAndPublishedTrue(ContentType contentType);
}
