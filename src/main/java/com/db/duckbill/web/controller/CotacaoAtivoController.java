package com.db.duckbill.web.controller;

import com.db.duckbill.service.CotacaoService;
import com.db.duckbill.web.dto.CotacaoAtivoDTO;
import com.db.duckbill.web.mapper.CotacaoAtivoMapper;
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
@RequestMapping("/api/v1/cotacoes-ativo")
public class CotacaoAtivoController {
    private final CotacaoService service;

    public CotacaoAtivoController(CotacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EntityModel<CotacaoAtivoDTO>> salvar(@RequestBody CotacaoAtivoDTO dto) {
        com.db.duckbill.domain.entity.CotacaoAtivo saved = service.salvarCotacaoAtivo(dto.ativoId(), dto.dataRef(), dto.precoFech());
        CotacaoAtivoDTO dtoSaved = CotacaoAtivoMapper.toDTO(saved);
        EntityModel<CotacaoAtivoDTO> model = EntityModel.of(dtoSaved,
            linkTo(methodOn(CotacaoAtivoController.class).buscar(saved.getId().getAtivoId(), saved.getId().getDataRef().toString())).withSelfRel(),
            linkTo(methodOn(CotacaoAtivoController.class).listar()).withRel("cotacoes-ativo")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping
    public CollectionModel<EntityModel<CotacaoAtivoDTO>> listar() {
        List<EntityModel<CotacaoAtivoDTO>> cotacoes = service.listarCotacoesAtivo().stream()
            .map(c -> EntityModel.of(CotacaoAtivoMapper.toDTO(c),
                linkTo(methodOn(CotacaoAtivoController.class).buscar(c.getId().getAtivoId(), c.getId().getDataRef().toString())).withSelfRel()
            ))
            .collect(Collectors.toList());
        return CollectionModel.of(cotacoes,
            linkTo(methodOn(CotacaoAtivoController.class).listar()).withSelfRel()
        );
    }

    @GetMapping("/{ativoId}/{dataRef}")
    public EntityModel<CotacaoAtivoDTO> buscar(@PathVariable Long ativoId, @PathVariable String dataRef) {
        java.time.LocalDate data = java.time.LocalDate.parse(dataRef);
        CotacaoAtivoDTO dto = CotacaoAtivoMapper.toDTO(service.buscarCotacaoAtivo(ativoId, data));
        return EntityModel.of(dto,
            linkTo(methodOn(CotacaoAtivoController.class).buscar(ativoId, dataRef)).withSelfRel(),
            linkTo(methodOn(CotacaoAtivoController.class).listar()).withRel("cotacoes-ativo")
        );
    }
}
