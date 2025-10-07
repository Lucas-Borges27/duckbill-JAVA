package com.db.duckbill.web.controller;

import com.db.duckbill.domain.entity.TransacaoAtivo;
import com.db.duckbill.service.TransacaoAtivoService;
import com.db.duckbill.web.dto.TransacaoAtivoDTO;
import com.db.duckbill.web.mapper.TransacaoAtivoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transacoes-ativo")
@RequiredArgsConstructor
public class TransacaoAtivoController {
    private final TransacaoAtivoService service;

    @PostMapping
    public ResponseEntity<TransacaoAtivoDTO> criar(@RequestBody TransacaoAtivoDTO dto) {
        TransacaoAtivo saved = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransacaoAtivoMapper.toDTO(saved));
    }

    @GetMapping
    public List<TransacaoAtivoDTO> listar() {
        return service.listar().stream().map(TransacaoAtivoMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoAtivoDTO> buscarPorId(@PathVariable Long id) {
        TransacaoAtivo transacao = service.buscarPorId(id);
        return ResponseEntity.ok(TransacaoAtivoMapper.toDTO(transacao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransacaoAtivoDTO> atualizar(@PathVariable Long id, @RequestBody TransacaoAtivoDTO dto) {
        TransacaoAtivo transacao = TransacaoAtivoMapper.toEntity(dto);
        TransacaoAtivo updated = service.atualizar(id, transacao);
        return ResponseEntity.ok(TransacaoAtivoMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
