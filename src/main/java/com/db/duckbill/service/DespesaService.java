package com.db.duckbill.service;

import com.db.duckbill.domain.entity.*;
import com.db.duckbill.domain.repo.*;
import com.db.duckbill.currency.CurrencyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DespesaService {
  private final DespesaRepository despesaRepo;
  private final UsuarioRepository usuarioRepo;
  private final CategoriaRepository categoriaRepo;
  private final CurrencyProvider currencyProvider;
  @Transactional
  public Despesa criar(Despesa d) {
    usuarioRepo.findById(d.getUsuario().getId()).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
    categoriaRepo.findById(d.getCategoria().getId()).orElseThrow(() -> new NoSuchElementException("Categoria não encontrada"));
    d.setMoeda(Optional.ofNullable(d.getMoeda()).orElse("BRL").toUpperCase());
    return despesaRepo.save(d);
  }
  public List<Despesa> listarMes(Long usuarioId, YearMonth ym) {
    return despesaRepo.findByUsuario_IdAndDataCompraBetween(usuarioId, ym.atDay(1), ym.atEndOfMonth());
  }
  public List<Map<String, Object>> top3Mes(Long usuarioId, YearMonth ym) {
    var despesas = listarMes(usuarioId, ym);
    return despesas.stream()
      .collect(Collectors.groupingBy(d -> d.getCategoria(), Collectors.reducing(BigDecimal.ZERO, Despesa::getValor, BigDecimal::add)))
      .entrySet().stream()
      .sorted(Map.Entry.<Categoria, BigDecimal>comparingByValue().reversed())
      .limit(3)
      .map(e -> {
        Map<String, Object> map = new HashMap<>();
        map.put("categoria", e.getKey().getNome());
        map.put("total", e.getValue());
        return map;
      })
      .collect(Collectors.toList());
  }
  public BigDecimal converter(BigDecimal valor, String from, String to) {
    if (from.equalsIgnoreCase(to)) return valor;
    return currencyProvider.convert(from, to, valor);
  }
  public List<String> insightsBasicos(Long usuarioId, YearMonth ym) {
    var despesas = listarMes(usuarioId, ym);
    BigDecimal total = despesas.stream().map(Despesa::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    if (total.signum() == 0) return List.of("Sem dados no período.");
    var porCat = despesas.stream().collect(Collectors.groupingBy(d -> d.getCategoria().getNome(), Collectors.reducing(BigDecimal.ZERO, Despesa::getValor, BigDecimal::add)));
    return porCat.entrySet().stream().sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed()).limit(3)
      .map(e -> String.format("Você gasta %s%% em %s. Vale reduzir?", e.getValue().multiply(BigDecimal.valueOf(100)).divide(total, 0, RoundingMode.HALF_UP), e.getKey()))
      .toList();
  }
}
