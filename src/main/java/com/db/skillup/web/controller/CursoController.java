package com.db.skillup.web.controller;

import com.db.skillup.domain.entity.Curso;
import com.db.skillup.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService service;
    private final PagedResourcesAssembler<Curso> cursoAssembler;

    @PostMapping
    public ResponseEntity<EntityModel<Curso>> criar(@Valid @RequestBody Curso curso) {
        Curso salvo = service.criar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(salvo));
    }

    @GetMapping
    public PagedModel<EntityModel<Curso>> listar(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String dificuldade,
            @ParameterObject Pageable pageable) {
        return cursoAssembler.toModel(service.listar(categoria, dificuldade, pageable), this::toModel);
    }

    @GetMapping("/{id}")
    public EntityModel<Curso> obter(@PathVariable Long id) {
        return toModel(service.buscarOuFalhar(id));
    }

    @PutMapping("/{id}")
    public EntityModel<Curso> atualizar(@PathVariable Long id, @Valid @RequestBody Curso curso) {
        return toModel(service.atualizar(id, curso));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }

    private EntityModel<Curso> toModel(Curso curso) {
        return EntityModel.of(curso,
                linkTo(methodOn(CursoController.class).obter(curso.getId())).withSelfRel(),
                linkTo(methodOn(CursoController.class).listar(null, null, Pageable.unpaged())).withRel("cursos"),
                linkTo(methodOn(ProgressoController.class).listar(null, curso.getId(), null, Pageable.unpaged())).withRel("progressos")
        );
    }
}
