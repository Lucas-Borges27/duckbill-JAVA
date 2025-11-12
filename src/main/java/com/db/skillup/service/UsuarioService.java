package com.db.skillup.service;

import com.db.skillup.domain.entity.Usuario;
import com.db.skillup.domain.repo.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Usuario criar(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return repository.save(usuario);
    }

    public Page<Usuario> listar(String areaInteresse, Pageable pageable) {
        if (areaInteresse == null || areaInteresse.isBlank()) {
            return repository.findAll(pageable);
        }
        return repository.findByAreaInteresseContainingIgnoreCase(areaInteresse, pageable);
    }

    public Usuario buscarOuFalhar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario existente = buscarOuFalhar(id);
        existente.setNome(dados.getNome());
        existente.setEmail(dados.getEmail());
        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(dados.getSenha()));
        }
        existente.setAreaInteresse(dados.getAreaInteresse());
        return repository.save(existente);
    }

    public void remover(Long id) {
        Usuario existente = buscarOuFalhar(id);
        repository.delete(existente);
    }
}
