package com.edusphere.user.entity;

import com.edusphere.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfile {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String encryptedFullName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String encryptedBio;
    private String interests;
    private String learningGoals;
    private Instant updatedAt = Instant.now();
}
