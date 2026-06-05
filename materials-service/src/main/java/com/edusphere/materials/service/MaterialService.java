package com.edusphere.materials.service;

import com.edusphere.common.exception.ResourceNotFoundException;
import com.edusphere.common.security.EncryptionService;
import com.edusphere.materials.dto.MaterialDto;
import com.edusphere.materials.entity.EducationalMaterial;
import com.edusphere.materials.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository repository;
    private final EncryptionService encryptionService;

    @Transactional
    public MaterialDto create(MaterialDto dto) {
        EducationalMaterial material = EducationalMaterial.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .subject(dto.getSubject())
                .contentType(dto.getContentType())
                .difficulty(dto.getDifficulty())
                .encryptedContentRef(encryptionService.encrypt(dto.getContent()))
                .authorId(dto.getAuthorId())
                .published(dto.isPublished())
                .build();
        return toDto(repository.save(material));
    }

    public MaterialDto getById(UUID id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found: " + id));
    }

    public List<MaterialDto> getBySubject(String subject) {
        return repository.findBySubjectAndPublishedTrue(subject).stream()
                .map(this::toDto).toList();
    }

    public List<MaterialDto> getAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    private MaterialDto toDto(EducationalMaterial m) {
        return MaterialDto.builder()
                .id(m.getId())
                .title(m.getTitle())
                .description(m.getDescription())
                .subject(m.getSubject())
                .contentType(m.getContentType())
                .difficulty(m.getDifficulty())
                .content(encryptionService.decrypt(m.getEncryptedContentRef()))
                .authorId(m.getAuthorId())
                .published(m.isPublished())
                .build();
    }
}
