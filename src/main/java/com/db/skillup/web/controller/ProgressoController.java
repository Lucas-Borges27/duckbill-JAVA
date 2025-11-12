package com.db.skillup.web.controller;

import com.db.skillup.domain.entity.Progresso;
import com.db.skillup.service.ProgressoService;
import com.db.skillup.web.dto.ProgressoCreateRequest;
import com.db.skillup.web.dto.ProgressoDTO;
import com.db.skillup.web.dto.ProgressoUpdateRequest;
import com.db.skillup.web.mapper.ProgressoMapper;
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
@RequestMapping("/api/v1/progressos")
@RequiredArgsConstructor
public class ProgressoController {

    private final ProgressoService service;
    private final ProgressoMapper mapper;
    private final PagedResourcesAssembler<Progresso> progressoAssembler;

    @PostMapping
    public ResponseEntity<EntityModel<ProgressoDTO>> criar(@Valid @RequestBody ProgressoCreateRequest request) {
        Progresso progresso = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(progresso));
    }

    @GetMapping
    public PagedModel<EntityModel<ProgressoDTO>> listar(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) Long cursoId,
            @RequestParam(required = false) String status,
            @ParameterObject Pageable pageable) {
        return progressoAssembler.toModel(service.listar(usuarioId, cursoId, status, pageable), this::toModel);
    }

    @GetMapping("/{id}")
    public EntityModel<ProgressoDTO> obter(@PathVariable Long id) {
        return toModel(service.buscarOuFalhar(id));
    }

    @PutMapping("/{id}")
    public EntityModel<ProgressoDTO> atualizar(@PathVariable Long id,
                                               @Valid @RequestBody ProgressoUpdateRequest request) {
        Progresso progresso = service.atualizar(id, request);
        return toModel(progresso);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }

    private EntityModel<ProgressoDTO> toModel(Progresso progresso) {
        ProgressoDTO dto = mapper.toDto(progresso);
        return EntityModel.of(dto,
                linkTo(methodOn(ProgressoController.class).obter(dto.id())).withSelfRel(),
                linkTo(methodOn(ProgressoController.class).listar(dto.usuarioId(), null, null, Pageable.unpaged())).withRel("usuario-progressos"),
                linkTo(methodOn(ProgressoController.class).listar(null, dto.cursoId(), null, Pageable.unpaged())).withRel("curso-progressos"),
                linkTo(methodOn(UsuarioController.class).obter(dto.usuarioId())).withRel("usuario"),
                linkTo(methodOn(CursoController.class).obter(dto.cursoId())).withRel("curso")
        );
    }
}
