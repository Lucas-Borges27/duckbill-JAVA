package com.db.duckbill.web.controller;

import com.db.duckbill.domain.entity.Ativo;
import com.db.duckbill.service.AtivoService;
import com.db.duckbill.web.dto.AtivoDTO;
import com.db.duckbill.web.mapper.AtivoMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/ativos")
public class AtivoController {
    private final AtivoService service;

    public AtivoController(AtivoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EntityModel<AtivoDTO>> criar(@RequestBody AtivoDTO dto) {
        Ativo saved = service.criar(AtivoMapper.toEntity(dto));
        AtivoDTO dtoSaved = AtivoMapper.toDTO(saved);
        EntityModel<AtivoDTO> model = EntityModel.of(dtoSaved,
            linkTo(methodOn(AtivoController.class).buscarPorId(saved.getId())).withSelfRel(),
            linkTo(methodOn(AtivoController.class).listar()).withRel("ativos"),
            linkTo(methodOn(TransacaoAtivoController.class).listar()).withRel("transacoes")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping
    public CollectionModel<EntityModel<AtivoDTO>> listar() {
        List<EntityModel<AtivoDTO>> ativos = service.listar().stream()
            .map(a -> EntityModel.of(AtivoMapper.toDTO(a),
                linkTo(methodOn(AtivoController.class).buscarPorId(a.getId())).withSelfRel(),
                linkTo(methodOn(TransacaoAtivoController.class).listar()).withRel("transacoes")
            ))
            .collect(Collectors.toList());
        return CollectionModel.of(ativos,
            linkTo(methodOn(AtivoController.class).listar()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<AtivoDTO> buscarPorId(@PathVariable Long id) {
        Ativo ativo = service.buscarPorId(id);
        AtivoDTO dto = AtivoMapper.toDTO(ativo);
        return EntityModel.of(dto,
            linkTo(methodOn(AtivoController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(AtivoController.class).listar()).withRel("ativos"),
            linkTo(methodOn(TransacaoAtivoController.class).listar()).withRel("transacoes")
        );
    }

    @PutMapping("/{id}")
    public EntityModel<AtivoDTO> atualizar(@PathVariable Long id, @RequestBody AtivoDTO dto) {
        Ativo ativo = AtivoMapper.toEntity(dto);
        AtivoDTO updated = AtivoMapper.toDTO(service.atualizar(id, ativo));
        return EntityModel.of(updated,
            linkTo(methodOn(AtivoController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(AtivoController.class).listar()).withRel("ativos"),
            linkTo(methodOn(TransacaoAtivoController.class).listar()).withRel("transacoes")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
