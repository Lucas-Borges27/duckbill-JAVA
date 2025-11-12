package com.db.skillup.domain.repo;

import com.db.skillup.domain.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Page<Usuario> findByAreaInteresseContainingIgnoreCase(String areaInteresse, Pageable pageable);
}
