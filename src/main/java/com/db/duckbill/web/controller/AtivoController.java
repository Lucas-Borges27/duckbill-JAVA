package com.db.duckbill.web.controller;

import com.db.duckbill.domain.entity.Ativo;
import com.db.duckbill.service.AtivoService;
import com.db.duckbill.web.dto.AtivoDTO;
import com.db.duckbill.web.mapper.AtivoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ativos")
public class AtivoController {
    private final AtivoService service;

    public AtivoController(AtivoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AtivoDTO> criar(@RequestBody AtivoDTO dto) {
        Ativo saved = service.criar(AtivoMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(AtivoMapper.toDTO(saved));
    }

    @GetMapping
    public List<AtivoDTO> listar() {
        return service.listar().stream().map(AtivoMapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AtivoDTO buscarPorId(@PathVariable Long id) {
        return AtivoMapper.toDTO(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public AtivoDTO atualizar(@PathVariable Long id, @RequestBody AtivoDTO dto) {
        Ativo ativo = AtivoMapper.toEntity(dto);
        return AtivoMapper.toDTO(service.atualizar(id, ativo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
