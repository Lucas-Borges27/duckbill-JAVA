package com.db.duckbill.web.controller.api;

import com.db.duckbill.domain.entity.TarefaFinanceira;
import com.db.duckbill.service.CurrentUserService;
import com.db.duckbill.service.TarefaFinanceiraService;
import com.db.duckbill.web.dto.TarefaFinanceiraDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tarefas")
@RequiredArgsConstructor
public class TarefaFinanceiraController {
    private final TarefaFinanceiraService tarefaService;
    private final CurrentUserService currentUserService;

    @PostMapping
    public ResponseEntity<TarefaFinanceiraDTO> criar(@Valid @RequestBody TarefaFinanceiraDTO dto) {
        Long usuarioId = currentUserService.resolveAccessibleUserId(dto.usuarioId());
        TarefaFinanceira tarefa = tarefaService.criar(usuarioId, toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(tarefa));
    }

    @GetMapping
    public List<TarefaFinanceiraDTO> listar(@RequestParam(required = false) Long usuarioId) {
        Long alvo = currentUserService.resolveAccessibleUserId(usuarioId);
        return tarefaService.listarPorUsuario(alvo).stream()
            .map(this::toDto)
            .toList();
    }

    @GetMapping("/notificacoes")
    public List<TarefaFinanceiraDTO> listarNotificacoes(@RequestParam(required = false) Long usuarioId) {
        Long alvo = currentUserService.resolveAccessibleUserId(usuarioId);
        return tarefaService.listarNotificacoes(alvo).stream()
            .map(this::toDto)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaFinanceiraDTO> obter(@PathVariable Long id) {
        TarefaFinanceira tarefa = tarefaService.buscarPorIdAutorizada(id, currentUserService.getUsuarioIdAtual(), currentUserService.isAdmin());
        return ResponseEntity.ok(toDto(tarefa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaFinanceiraDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TarefaFinanceiraDTO dto) {
        Long usuarioId = currentUserService.resolveAccessibleUserId(dto.usuarioId());
        TarefaFinanceira tarefa = tarefaService.atualizar(id, usuarioId, currentUserService.isAdmin(), toEntity(dto));
        return ResponseEntity.ok(toDto(tarefa));
    }

    @PostMapping("/{id}/concluir")
    public ResponseEntity<TarefaFinanceiraDTO> concluir(@PathVariable Long id) {
        TarefaFinanceira tarefa = tarefaService.concluir(id, currentUserService.getUsuarioIdAtual(), currentUserService.isAdmin());
        return ResponseEntity.ok(toDto(tarefa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tarefaService.deletar(id, currentUserService.getUsuarioIdAtual(), currentUserService.isAdmin());
        return ResponseEntity.noContent().build();
    }

    private TarefaFinanceira toEntity(TarefaFinanceiraDTO dto) {
        TarefaFinanceira tarefa = new TarefaFinanceira();
        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setValorEstimado(dto.valorEstimado());
        tarefa.setDataLimite(dto.dataLimite());
        tarefa.setNotificarEm(dto.notificarEm());
        tarefa.setStatus(dto.status());
        return tarefa;
    }

    private TarefaFinanceiraDTO toDto(TarefaFinanceira tarefa) {
        return TarefaFinanceiraDTO.fromEntity(tarefa, tarefaService.calcularSituacao(tarefa));
    }
}
