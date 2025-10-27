package com.db.duckbill.web.controller;

import com.db.duckbill.domain.entity.Usuario;
import com.db.duckbill.domain.repo.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioRepository repo;

    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> criar(@RequestBody Usuario u) {
        Usuario saved = repo.save(u);
        EntityModel<Usuario> model = EntityModel.of(saved,
            linkTo(methodOn(UsuarioController.class).obter(saved.getId())).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios"),
            linkTo(methodOn(DespesaController.class).listar(saved.getId(), null)).withRel("despesas")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping
    public CollectionModel<EntityModel<Usuario>> listar() {
        List<EntityModel<Usuario>> usuarios = repo.findAll().stream()
            .map(u -> EntityModel.of(u,
                linkTo(methodOn(UsuarioController.class).obter(u.getId())).withSelfRel(),
                linkTo(methodOn(DespesaController.class).listar(u.getId(), null)).withRel("despesas")
            ))
            .collect(Collectors.toList());
        return CollectionModel.of(usuarios,
            linkTo(methodOn(UsuarioController.class).listar()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obter(@PathVariable Long id) {
        return repo.findById(id)
            .map(u -> EntityModel.of(u,
                linkTo(methodOn(UsuarioController.class).obter(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios"),
                linkTo(methodOn(DespesaController.class).listar(id, null)).withRel("despesas")
            ))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
