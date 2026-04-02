package com.db.duckbill.web.controller.api;

import com.db.duckbill.service.CurrentUserService;
import com.db.duckbill.service.UsuarioService;
import com.db.duckbill.web.dto.MeUpdateRequest;
import com.db.duckbill.web.dto.UsuarioDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class MeController {
    private final CurrentUserService currentUserService;
    private final UsuarioService usuarioService;

    @GetMapping
    public UsuarioDTO me() {
        return UsuarioDTO.fromEntity(currentUserService.getUsuarioAtual());
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizar(@Valid @RequestBody MeUpdateRequest request) {
        Long usuarioId = currentUserService.getUsuarioIdAtual();
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuarioService.atualizarSaldo(usuarioId, request.saldo())));
    }
}
