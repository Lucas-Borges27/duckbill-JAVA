package com.db.skillup.domain.repo;

import com.db.skillup.domain.entity.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    Page<Curso> findByCategoriaContainingIgnoreCase(String categoria, Pageable pageable);
    Page<Curso> findByDificuldadeIgnoreCase(String dificuldade, Pageable pageable);
    Page<Curso> findByCategoriaContainingIgnoreCaseAndDificuldadeIgnoreCase(String categoria, String dificuldade, Pageable pageable);
}
