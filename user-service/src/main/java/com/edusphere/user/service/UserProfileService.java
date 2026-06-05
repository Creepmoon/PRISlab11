package com.edusphere.user.service;

import com.edusphere.common.exception.ResourceNotFoundException;
import com.edusphere.common.security.EncryptionService;
import com.edusphere.user.dto.UserProfileDto;
import com.edusphere.user.entity.UserProfile;
import com.edusphere.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository repository;
    private final EncryptionService encryptionService;

    @Transactional
    public UserProfileDto createOrUpdate(UserProfileDto dto) {
        UserProfile profile = repository.findById(dto.getId())
                .orElse(UserProfile.builder().id(dto.getId()).build());

        profile.setEncryptedFullName(encryptionService.encrypt(dto.getFullName()));
        profile.setRole(dto.getRole());
        if (dto.getBio() != null) {
            profile.setEncryptedBio(encryptionService.encrypt(dto.getBio()));
        }
        profile.setInterests(dto.getInterests());
        profile.setLearningGoals(dto.getLearningGoals());
        profile.setUpdatedAt(Instant.now());

        repository.save(profile);
        return toDto(profile);
    }

    public UserProfileDto getById(UUID id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    private UserProfileDto toDto(UserProfile p) {
        return UserProfileDto.builder()
                .id(p.getId())
                .fullName(encryptionService.decrypt(p.getEncryptedFullName()))
                .role(p.getRole())
                .bio(p.getEncryptedBio() != null ? encryptionService.decrypt(p.getEncryptedBio()) : null)
                .interests(p.getInterests())
                .learningGoals(p.getLearningGoals())
                .build();
    }
}
