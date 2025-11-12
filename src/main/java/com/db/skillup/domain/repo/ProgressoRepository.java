package com.db.skillup.domain.repo;

import com.db.skillup.domain.entity.Progresso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressoRepository extends JpaRepository<Progresso, Long> {
    Page<Progresso> findByUsuarioId(Long usuarioId, Pageable pageable);
    Page<Progresso> findByCursoId(Long cursoId, Pageable pageable);
    Page<Progresso> findByStatusIgnoreCase(String status, Pageable pageable);
    Optional<Progresso> findByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);
}
