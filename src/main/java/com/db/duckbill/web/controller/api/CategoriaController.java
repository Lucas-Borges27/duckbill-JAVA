package com.db.duckbill.web.controller.api;

import com.db.duckbill.domain.entity.Categoria;
import com.db.duckbill.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<Categoria>> criar(@Valid @RequestBody Categoria c) {
        Categoria saved = service.criar(c);
        EntityModel<Categoria> model = EntityModel.of(saved,
            linkTo(methodOn(CategoriaController.class).listar()).withRel("categorias")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping
    public CollectionModel<EntityModel<Categoria>> listar() {
        List<EntityModel<Categoria>> categorias = service.listar().stream()
            .map(c -> EntityModel.of(c,
                linkTo(methodOn(CategoriaController.class).listar()).withRel("categorias")
            ))
            .collect(Collectors.toList());
        return CollectionModel.of(categorias,
            linkTo(methodOn(CategoriaController.class).listar()).withSelfRel()
        );
    }
}
