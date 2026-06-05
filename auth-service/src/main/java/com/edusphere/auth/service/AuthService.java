package com.edusphere.auth.service;

import com.edusphere.auth.dto.AuthRequest;
import com.edusphere.auth.dto.AuthResponse;
import com.edusphere.auth.entity.UserAccount;
import com.edusphere.auth.repository.UserAccountRepository;
import com.edusphere.auth.security.JwtService;
import com.edusphere.common.enums.UserRole;
import com.edusphere.common.security.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EncryptionService encryptionService;

    @Transactional
    public AuthResponse register(AuthRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        UserRole role = request.getRole() != null ? request.getRole() : UserRole.STUDENT;

        UserAccount account = UserAccount.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .encryptedFullName(encryptionService.encrypt(
                        request.getFullName() != null ? request.getFullName() : request.getEmail()))
                .role(role)
                .build();

        account = repository.save(account);
        return buildResponse(account);
    }

    public AuthResponse login(AuthRequest request) {
        UserAccount account = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        if (!account.isActive()) {
            throw new IllegalStateException("Account is deactivated");
        }
        return buildResponse(account);
    }

    private AuthResponse buildResponse(UserAccount account) {
        String token = jwtService.generateToken(account.getId(), account.getEmail(), account.getRole());
        return AuthResponse.builder()
                .token(token)
                .userId(account.getId())
                .email(account.getEmail())
                .fullName(encryptionService.decrypt(account.getEncryptedFullName()))
                .role(account.getRole())
                .expiresIn(jwtService.getExpirationMs())
                .build();
    }
}
