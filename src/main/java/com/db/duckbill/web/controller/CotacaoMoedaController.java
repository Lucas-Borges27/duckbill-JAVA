package com.db.duckbill.web.controller;

import com.db.duckbill.service.CotacaoMoedaService;
import com.db.duckbill.web.dto.CotacaoMoedaDTO;
import com.db.duckbill.web.mapper.CotacaoMoedaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cotacoes-moeda")
@RequiredArgsConstructor
public class CotacaoMoedaController {
    private final CotacaoMoedaService service;

    @GetMapping
    public List<CotacaoMoedaDTO> listar() {
        return service.listar().stream().map(CotacaoMoedaMapper::toDTO).toList();
    }

    @GetMapping("/{moeda}/{dataRef}")
    public ResponseEntity<CotacaoMoedaDTO> buscar(@PathVariable String moeda, @PathVariable String dataRef) {
        LocalDate data = LocalDate.parse(dataRef);
        Optional<com.db.duckbill.domain.entity.CotacaoMoeda> cotacao = service.buscarPorMoedaEData(moeda.toUpperCase(), data);
        if (cotacao.isPresent()) {
            return ResponseEntity.ok(CotacaoMoedaMapper.toDTO(cotacao.get()));
        } else {
            BigDecimal valor = service.obterCotacaoExterna(moeda.toUpperCase(), data);
            CotacaoMoedaDTO dto = new CotacaoMoedaDTO(moeda.toUpperCase(), data, valor);
            return ResponseEntity.ok(dto);
        }
    }
}
