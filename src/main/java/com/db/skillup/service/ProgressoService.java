package com.db.skillup.service;

import com.db.skillup.domain.entity.Curso;
import com.db.skillup.domain.entity.Progresso;
import com.db.skillup.domain.entity.Usuario;
import com.db.skillup.domain.repo.CursoRepository;
import com.db.skillup.domain.repo.ProgressoRepository;
import com.db.skillup.domain.repo.UsuarioRepository;
import com.db.skillup.web.dto.ProgressoCreateRequest;
import com.db.skillup.web.dto.ProgressoUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressoService {

    private final ProgressoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public Progresso criar(ProgressoCreateRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + request.usuarioId()));
        Curso curso = cursoRepository.findById(request.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado: " + request.cursoId()));

        Progresso progresso = new Progresso();
        progresso.setUsuario(usuario);
        progresso.setCurso(curso);
        progresso.setStatus(request.status());
        progresso.setDataInicio(request.dataInicio());
        progresso.setDataFim(request.dataFim());
        progresso.setPorcentagem(request.porcentagem());

        validarDatas(progresso.getDataInicio(), progresso.getDataFim());

        return repository.save(progresso);
    }

    public Page<Progresso> listar(Long usuarioId, Long cursoId, String status, Pageable pageable) {
        if (usuarioId != null && cursoId != null) {
            return repository.findByUsuarioIdAndCursoId(usuarioId, cursoId)
                    .map(p -> new PageImpl<>(List.of(p), pageable, 1))
                    .orElseGet(() -> new PageImpl<>(List.of(), pageable, 0));
        }
        if (usuarioId != null) {
            return repository.findByUsuarioId(usuarioId, pageable);
        }
        if (cursoId != null) {
            return repository.findByCursoId(cursoId, pageable);
        }
        if (StringUtils.hasText(status)) {
            return repository.findByStatusIgnoreCase(status, pageable);
        }
        return repository.findAll(pageable);
    }

    public Progresso buscarOuFalhar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Progresso não encontrado: " + id));
    }

    public Progresso atualizar(Long id, ProgressoUpdateRequest request) {
        Progresso progresso = buscarOuFalhar(id);
        if (StringUtils.hasText(request.status())) {
            progresso.setStatus(request.status());
        }
        if (request.dataInicio() != null) {
            progresso.setDataInicio(request.dataInicio());
        }
        if (request.dataFim() != null) {
            progresso.setDataFim(request.dataFim());
        }
        if (request.porcentagem() != null) {
            progresso.setPorcentagem(request.porcentagem());
        }

        validarDatas(progresso.getDataInicio(), progresso.getDataFim());

        return repository.save(progresso);
    }

    public void remover(Long id) {
        Progresso progresso = buscarOuFalhar(id);
        repository.delete(progresso);
    }

    private void validarDatas(LocalDate inicio, LocalDate fim) {
        if (inicio != null && fim != null && fim.isBefore(inicio)) {
            throw new IllegalArgumentException("dataFim não pode ser anterior a dataInicio");
        }
    }
}
