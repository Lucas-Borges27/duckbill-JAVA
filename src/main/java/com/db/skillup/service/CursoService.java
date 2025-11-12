package com.db.skillup.service;

import com.db.skillup.domain.entity.Curso;
import com.db.skillup.domain.repo.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository repository;

    public Curso criar(Curso curso) {
        return repository.save(curso);
    }

    public Page<Curso> listar(String categoria, String dificuldade, Pageable pageable) {
        boolean hasCategoria = categoria != null && !categoria.isBlank();
        boolean hasDificuldade = dificuldade != null && !dificuldade.isBlank();

        if (hasCategoria && hasDificuldade) {
            return repository.findByCategoriaContainingIgnoreCaseAndDificuldadeIgnoreCase(categoria, dificuldade, pageable);
        }
        if (hasCategoria) {
            return repository.findByCategoriaContainingIgnoreCase(categoria, pageable);
        }
        if (hasDificuldade) {
            return repository.findByDificuldadeIgnoreCase(dificuldade, pageable);
        }
        return repository.findAll(pageable);
    }

    public Curso buscarOuFalhar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado: " + id));
    }

    public Curso atualizar(Long id, Curso dados) {
        Curso existente = buscarOuFalhar(id);
        existente.setNome(dados.getNome());
        existente.setCategoria(dados.getCategoria());
        existente.setCargaHoraria(dados.getCargaHoraria());
        existente.setDificuldade(dados.getDificuldade());
        existente.setDescricao(dados.getDescricao());
        return repository.save(existente);
    }

    public void remover(Long id) {
        Curso existente = buscarOuFalhar(id);
        repository.delete(existente);
    }
}
