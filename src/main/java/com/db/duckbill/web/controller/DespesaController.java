package com.db.duckbill.web.controller;

import com.db.duckbill.service.DespesaService;
import com.db.duckbill.web.dto.DespesaDTO;
import com.db.duckbill.web.mapper.DespesaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/despesas")
@RequiredArgsConstructor
public class DespesaController {
    private final DespesaService service;

    @PostMapping
    public ResponseEntity<DespesaDTO> criar(@RequestBody DespesaDTO dto) {
        var saved = service.criar(DespesaMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(DespesaMapper.toDTO(saved));
    }

    @GetMapping
    public List<DespesaDTO> listar(@RequestParam Long usuarioId, @RequestParam String mes) {
        var ym = YearMonth.parse(mes);
        return service.listarMes(usuarioId, ym).stream().map(DespesaMapper::toDTO).toList();
    }

    @GetMapping("/top3")
    public List<Map<String, Object>> top3(@RequestParam Long usuarioId, @RequestParam String mes) {
        var ym = YearMonth.parse(mes);
        return service.top3Mes(usuarioId, ym);
    }

    @GetMapping("/insights")
    public List<String> insights(@RequestParam Long usuarioId, @RequestParam String mes) {
        return service.insightsBasicos(usuarioId, YearMonth.parse(mes));
    }
}
