package com.db.duckbill.web.controller;

import com.db.duckbill.domain.entity.Categoria;
import com.db.duckbill.domain.repo.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaRepository repo;

    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody Categoria c) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(c));
    }

    @GetMapping
    public List<Categoria> listar() {
        return repo.findAll();
    }
}
