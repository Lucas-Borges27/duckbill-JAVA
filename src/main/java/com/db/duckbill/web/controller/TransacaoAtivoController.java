package com.db.duckbill.web.controller;

import com.db.duckbill.domain.entity.TransacaoAtivo;
import com.db.duckbill.service.TransacaoAtivoService;
import com.db.duckbill.web.dto.TransacaoAtivoDTO;
import com.db.duckbill.web.mapper.TransacaoAtivoMapper;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/transacoes-ativo")
@RequiredArgsConstructor
public class TransacaoAtivoController {
    private final TransacaoAtivoService service;

    @PostMapping
    public ResponseEntity<EntityModel<TransacaoAtivoDTO>> criar(@RequestBody TransacaoAtivoDTO dto) {
        TransacaoAtivo saved = service.criar(dto);
        TransacaoAtivoDTO dtoSaved = TransacaoAtivoMapper.toDTO(saved);
        EntityModel<TransacaoAtivoDTO> model = EntityModel.of(dtoSaved,
            linkTo(methodOn(TransacaoAtivoController.class).buscarPorId(saved.getId())).withSelfRel(),
            linkTo(methodOn(TransacaoAtivoController.class).listar()).withRel("transacoes-ativo"),
            linkTo(methodOn(AtivoController.class).buscarPorId(saved.getAtivo().getId())).withRel("ativo"),
            linkTo(methodOn(UsuarioController.class).obter(saved.getUsuario().getId())).withRel("usuario")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping
    public CollectionModel<EntityModel<TransacaoAtivoDTO>> listar() {
        List<EntityModel<TransacaoAtivoDTO>> transacoes = service.listar().stream()
            .map(t -> EntityModel.of(TransacaoAtivoMapper.toDTO(t),
                linkTo(methodOn(TransacaoAtivoController.class).buscarPorId(t.getId())).withSelfRel(),
                linkTo(methodOn(AtivoController.class).buscarPorId(t.getAtivo().getId())).withRel("ativo"),
                linkTo(methodOn(UsuarioController.class).obter(t.getUsuario().getId())).withRel("usuario")
            ))
            .collect(Collectors.toList());
        return CollectionModel.of(transacoes,
            linkTo(methodOn(TransacaoAtivoController.class).listar()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TransacaoAtivoDTO>> buscarPorId(@PathVariable Long id) {
        TransacaoAtivo transacao = service.buscarPorId(id);
        TransacaoAtivoDTO dto = TransacaoAtivoMapper.toDTO(transacao);
        EntityModel<TransacaoAtivoDTO> model = EntityModel.of(dto,
            linkTo(methodOn(TransacaoAtivoController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(TransacaoAtivoController.class).listar()).withRel("transacoes-ativo"),
            linkTo(methodOn(AtivoController.class).buscarPorId(transacao.getAtivo().getId())).withRel("ativo"),
            linkTo(methodOn(UsuarioController.class).obter(transacao.getUsuario().getId())).withRel("usuario")
        );
        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<TransacaoAtivoDTO>> atualizar(@PathVariable Long id, @RequestBody TransacaoAtivoDTO dto) {
        TransacaoAtivo transacao = TransacaoAtivoMapper.toEntity(dto);
        TransacaoAtivo updated = service.atualizar(id, transacao);
        TransacaoAtivoDTO dtoUpdated = TransacaoAtivoMapper.toDTO(updated);
        EntityModel<TransacaoAtivoDTO> model = EntityModel.of(dtoUpdated,
            linkTo(methodOn(TransacaoAtivoController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(TransacaoAtivoController.class).listar()).withRel("transacoes-ativo"),
            linkTo(methodOn(AtivoController.class).buscarPorId(updated.getAtivo().getId())).withRel("ativo"),
            linkTo(methodOn(UsuarioController.class).obter(updated.getUsuario().getId())).withRel("usuario")
        );
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
