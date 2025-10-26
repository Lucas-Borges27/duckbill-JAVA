package com.db.duckbill.web.controller;

import com.db.duckbill.service.DespesaService;
import com.db.duckbill.web.dto.DespesaDTO;
import com.db.duckbill.web.mapper.DespesaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/despesas")
@RequiredArgsConstructor
public class DespesaController {
    private final DespesaService service;

    @PostMapping
    public ResponseEntity<EntityModel<DespesaDTO>> criar(@RequestBody DespesaDTO dto) {
        var saved = service.criar(DespesaMapper.toEntity(dto));
        DespesaDTO dtoSaved = DespesaMapper.toDTO(saved);
        EntityModel<DespesaDTO> model = EntityModel.of(dtoSaved,
            linkTo(methodOn(DespesaController.class).listar(saved.getUsuario().getId(), null)).withRel("despesas"),
            linkTo(methodOn(UsuarioController.class).obter(saved.getUsuario().getId())).withRel("usuario"),
            linkTo(methodOn(CategoriaController.class).listar()).withRel("categorias")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping
    public CollectionModel<EntityModel<DespesaDTO>> listar(@RequestParam Long usuarioId, @RequestParam String mes) {
        var ym = YearMonth.parse(mes);
        List<EntityModel<DespesaDTO>> despesas = service.listarMes(usuarioId, ym).stream()
            .map(d -> EntityModel.of(DespesaMapper.toDTO(d),
                linkTo(methodOn(UsuarioController.class).obter(d.getUsuario().getId())).withRel("usuario"),
                linkTo(methodOn(CategoriaController.class).listar()).withRel("categorias")
            ))
            .collect(Collectors.toList());
        return CollectionModel.of(despesas,
            linkTo(methodOn(DespesaController.class).listar(usuarioId, mes)).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).obter(usuarioId)).withRel("usuario")
        );
    }

    @GetMapping("/top3")
    public CollectionModel<EntityModel<Map<String, Object>>> top3(@RequestParam Long usuarioId, @RequestParam String mes) {
        var ym = YearMonth.parse(mes);
        List<EntityModel<Map<String, Object>>> top3 = service.top3Mes(usuarioId, ym).stream()
            .map(t -> EntityModel.of(t,
                linkTo(methodOn(DespesaController.class).listar(usuarioId, mes)).withRel("despesas")
            ))
            .collect(Collectors.toList());
        return CollectionModel.of(top3,
            linkTo(methodOn(DespesaController.class).top3(usuarioId, mes)).withSelfRel()
        );
    }

    @GetMapping("/insights")
    public CollectionModel<EntityModel<String>> insights(@RequestParam Long usuarioId, @RequestParam String mes) {
        List<EntityModel<String>> insights = service.insightsBasicos(usuarioId, YearMonth.parse(mes)).stream()
            .map(i -> EntityModel.of(i,
                linkTo(methodOn(DespesaController.class).listar(usuarioId, mes)).withRel("despesas")
            ))
            .collect(Collectors.toList());
        return CollectionModel.of(insights,
            linkTo(methodOn(DespesaController.class).insights(usuarioId, mes)).withSelfRel()
        );
    }
}
