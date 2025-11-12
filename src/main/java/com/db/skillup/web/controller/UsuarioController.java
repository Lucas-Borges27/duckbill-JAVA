package com.db.skillup.web.controller;

import com.db.skillup.domain.entity.Usuario;
import com.db.skillup.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Validated
public class UsuarioController {

    private final UsuarioService service;
    private final PagedResourcesAssembler<Usuario> usuarioAssembler;

    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> criar(@Valid @RequestBody Usuario usuario) {
        Usuario salvo = service.criar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(salvo));
    }

    @GetMapping
    public PagedModel<EntityModel<Usuario>> listar(@RequestParam(required = false) String areaInteresse,
                                                   @ParameterObject Pageable pageable) {
        return usuarioAssembler.toModel(service.listar(areaInteresse, pageable), this::toModel);
    }

    @GetMapping("/{id}")
    public EntityModel<Usuario> obter(@PathVariable Long id) {
        return toModel(service.buscarOuFalhar(id));
    }

    @PutMapping("/{id}")
    public EntityModel<Usuario> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        Usuario atualizado = service.atualizar(id, usuario);
        return toModel(atualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }

    private EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obter(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listar(null, Pageable.unpaged())).withRel("usuarios"),
                linkTo(methodOn(ProgressoController.class).listar(usuario.getId(), null, null, Pageable.unpaged())).withRel("progressos")
        );
    }
}
