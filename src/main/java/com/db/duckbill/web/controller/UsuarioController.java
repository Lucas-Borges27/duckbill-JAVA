package com.db.duckbill.web.controller;

import com.db.duckbill.domain.entity.Usuario;
import com.db.duckbill.domain.repo.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
public class UsuarioController {
    private final UsuarioRepository repo;

    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário")
    public ResponseEntity<Usuario> criar(@RequestBody Usuario u) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(u));
    }

    @GetMapping
    @Operation(summary = "Listar usuários", description = "Retorna lista de todos os usuários")
    public List<Usuario> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter usuário", description = "Retorna usuário por ID")
    public ResponseEntity<Usuario> obter(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
