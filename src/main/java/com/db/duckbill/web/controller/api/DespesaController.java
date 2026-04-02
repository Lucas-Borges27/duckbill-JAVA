package com.db.duckbill.web.controller.api;

import com.db.duckbill.service.CurrentUserService;
import com.db.duckbill.service.DashboardService;
import com.db.duckbill.service.DespesaService;
import com.db.duckbill.web.dto.DespesaDTO;
import com.db.duckbill.web.mapper.DespesaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/despesas")
@RequiredArgsConstructor
public class DespesaController {
    private final DespesaService service;
    private final DashboardService dashboardService;
    private final CurrentUserService currentUserService;

    @PostMapping
    public ResponseEntity<DespesaDTO> criar(@Valid @RequestBody DespesaDTO dto) {
        Long usuarioId = currentUserService.resolveAccessibleUserId(dto.usuarioId());
        var saved = service.criar(DespesaMapper.toEntity(withUsuario(dto, usuarioId)));
        DespesaDTO dtoSaved = DespesaMapper.toDTO(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSaved);
    }

    @GetMapping
    public List<DespesaDTO> listar(@RequestParam(required = false) Long usuarioId, @RequestParam(required = false) String mes) {
        Long alvo = resolverUsuarioId(usuarioId);
        var ym = mes == null || mes.isBlank() ? YearMonth.now() : YearMonth.parse(mes);
        return service.listarMes(alvo, ym).stream()
            .map(DespesaMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDTO> obter(@PathVariable Long id) {
        var despesa = service.buscarPorId(id);
        currentUserService.validarAcessoAoUsuario(despesa.getUsuario().getId());
        return ResponseEntity.ok(DespesaMapper.toDTO(despesa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody DespesaDTO dto) {
        Long usuarioId = currentUserService.resolveAccessibleUserId(dto.usuarioId());
        var payload = DespesaMapper.toEntity(withUsuario(dto, usuarioId));

        return ResponseEntity.ok(DespesaMapper.toDTO(service.atualizar(id, usuarioId, payload)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        var despesa = service.buscarPorId(id);
        currentUserService.validarAcessoAoUsuario(despesa.getUsuario().getId());
        service.deletar(id, despesa.getUsuario().getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/top3")
    public List<Map<String, Object>> top3(@RequestParam(required = false) Long usuarioId, @RequestParam String mes) {
        Long alvo = resolverUsuarioId(usuarioId);
        var ym = YearMonth.parse(mes);
        return dashboardService.top3Mes(alvo, ym);
    }

    @GetMapping("/insights")
    public ResponseEntity<List<String>> insights(@RequestParam(required = false) Long usuarioId, @RequestParam String mes) {
        Long alvo = resolverUsuarioId(usuarioId);
        return ResponseEntity.ok(dashboardService.insightsBasicos(alvo, YearMonth.parse(mes)));
    }

    private Long resolverUsuarioId(Long usuarioId) {
        return currentUserService.resolveAccessibleUserId(usuarioId);
    }

    private DespesaDTO withUsuario(DespesaDTO dto, Long usuarioId) {
        return new DespesaDTO(
            dto.id(),
            usuarioId,
            dto.categoriaId(),
            dto.valor(),
            dto.moeda(),
            dto.dataCompra(),
            dto.descricao()
        );
    }
}
