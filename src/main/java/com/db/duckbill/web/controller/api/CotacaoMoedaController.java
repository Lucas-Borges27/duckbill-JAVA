package com.db.duckbill.web.controller.api;

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
            .map(cotacao -> EntityModel.of(CotacaoMoedaMapper.toDTO(cotacao),
                linkTo(methodOn(CotacaoMoedaController.class).buscar(cotacao.getId().getMoeda(), cotacao.getId().getDataRef().toString())).withSelfRel()
            ))
            .collect(Collectors.toList());

        return CollectionModel.of(cotacoes,
            linkTo(methodOn(CotacaoMoedaController.class).listar()).withSelfRel()
        );
    }

    @GetMapping("/{moeda}/{dataRef}")
    public ResponseEntity<EntityModel<CotacaoMoedaDTO>> buscar(@PathVariable String moeda, @PathVariable String dataRef) {
        LocalDate data = LocalDate.parse(dataRef);
        BigDecimal valor = service.obterCotacaoExterna(moeda.toUpperCase(), data);
        CotacaoMoedaDTO dto = new CotacaoMoedaDTO(moeda.toUpperCase(), data, valor);
        EntityModel<CotacaoMoedaDTO> model = EntityModel.of(dto,
            linkTo(methodOn(CotacaoMoedaController.class).buscar(moeda, dataRef)).withSelfRel(),
            linkTo(methodOn(CotacaoMoedaController.class).listar()).withRel("cotacoes-moeda")
        );
        return ResponseEntity.ok(model);
    }
}
