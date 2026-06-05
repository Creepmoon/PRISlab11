package com.edusphere.materials.entity;

import com.edusphere.common.enums.ContentType;
import com.edusphere.common.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "educational_materials")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EducationalMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String description;
    private String subject;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty;

    /** Зашифрованный путь/контент материала */
    @Column(columnDefinition = "TEXT")
    private String encryptedContentRef;

    private UUID authorId;
    private boolean published = true;
    private Instant createdAt = Instant.now();
}
