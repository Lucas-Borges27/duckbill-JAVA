package com.db.duckbill.web.controller;

import com.db.duckbill.service.CotacaoMoedaService;
import com.db.duckbill.web.dto.CotacaoMoedaDTO;
import com.db.duckbill.web.mapper.CotacaoMoedaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/cotacoes-moeda")
@RequiredArgsConstructor
public class CotacaoMoedaController {
    private final CotacaoMoedaService service;

    @GetMapping
    public CollectionModel<EntityModel<CotacaoMoedaDTO>> listar() {
        List<EntityModel<CotacaoMoedaDTO>> cotacoes = service.listar().stream()
            .map(c -> EntityModel.of(CotacaoMoedaMapper.toDTO(c),
                linkTo(methodOn(CotacaoMoedaController.class).buscar(c.getId().getMoeda(), c.getId().getDataRef().toString())).withSelfRel()
            ))
            .collect(Collectors.toList());
        return CollectionModel.of(cotacoes,
            linkTo(methodOn(CotacaoMoedaController.class).listar()).withSelfRel()
        );
    }

    @GetMapping("/{moeda}/{dataRef}")
    public ResponseEntity<EntityModel<CotacaoMoedaDTO>> buscar(@PathVariable String moeda, @PathVariable String dataRef) {
        LocalDate data = LocalDate.parse(dataRef);
        Optional<com.db.duckbill.domain.entity.CotacaoMoeda> cotacao = service.buscarPorMoedaEData(moeda.toUpperCase(), data);
        CotacaoMoedaDTO dto;
        if (cotacao.isPresent()) {
            dto = CotacaoMoedaMapper.toDTO(cotacao.get());
        } else {
            BigDecimal valor = service.obterCotacaoExterna(moeda.toUpperCase(), data);
            dto = new CotacaoMoedaDTO(moeda.toUpperCase(), data, valor);
        }
        EntityModel<CotacaoMoedaDTO> model = EntityModel.of(dto,
            linkTo(methodOn(CotacaoMoedaController.class).buscar(moeda, dataRef)).withSelfRel(),
            linkTo(methodOn(CotacaoMoedaController.class).listar()).withRel("cotacoes-moeda")
        );
        return ResponseEntity.ok(model);
    }
}
