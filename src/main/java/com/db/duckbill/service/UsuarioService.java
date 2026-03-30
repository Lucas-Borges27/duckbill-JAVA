package com.db.duckbill.service;

import com.db.duckbill.domain.entity.Usuario;
import com.db.duckbill.domain.repo.UsuarioRepository;
import com.db.duckbill.web.dto.UsuarioCreateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario criar(UsuarioCreateDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome().trim());
        usuario.setEmail(dto.email().trim().toLowerCase());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setRole("ROLE_USER");
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
    }
}
