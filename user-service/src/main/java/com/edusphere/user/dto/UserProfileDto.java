package com.edusphere.user.dto;

import com.edusphere.common.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserProfileDto {
    private UUID id;
    private String fullName;
    private UserRole role;
    private String bio;
    private String interests;
    private String learningGoals;
}
