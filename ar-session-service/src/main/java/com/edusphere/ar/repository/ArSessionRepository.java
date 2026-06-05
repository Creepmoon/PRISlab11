package com.edusphere.ar.repository;

import com.edusphere.ar.entity.ArClassSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ArSessionRepository extends JpaRepository<ArClassSession, UUID> {
    List<ArClassSession> findByStatus(String status);
}
