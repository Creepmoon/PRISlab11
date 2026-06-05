package com.edusphere.auth.dto;

import com.edusphere.common.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthResponse {
    private String token;
    private UUID userId;
    private String email;
    private String fullName;
    private UserRole role;
    private long expiresIn;
}
