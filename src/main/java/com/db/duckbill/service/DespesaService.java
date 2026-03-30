package com.db.duckbill.service;

import com.db.duckbill.domain.entity.*;
import com.db.duckbill.domain.repo.*;
import com.db.duckbill.web.exception.AcessoNegadoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DespesaService {
  private final DespesaRepository despesaRepo;
  private final UsuarioRepository usuarioRepo;
  private final CategoriaRepository categoriaRepo;
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
  public Despesa buscarPorId(Long id) {
    return despesaRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Despesa não encontrada"));
  }
  @Transactional
  public Despesa atualizar(Long id, Long usuarioId, Despesa dados) {
    Despesa existente = buscarPorId(id);
    if (!existente.getUsuario().getId().equals(usuarioId)) {
      throw new AcessoNegadoException("Acesso negado à despesa.");
    }
    existente.setCategoria(dados.getCategoria());
    existente.setValor(dados.getValor());
    existente.setMoeda(dados.getMoeda());
    existente.setDataCompra(dados.getDataCompra());
    existente.setDescricao(dados.getDescricao());
    return despesaRepo.save(existente);
  }
  @Transactional
  public void deletar(Long id, Long usuarioId) {
    Despesa existente = buscarPorId(id);
    if (!existente.getUsuario().getId().equals(usuarioId)) {
      throw new AcessoNegadoException("Acesso negado à despesa.");
    }
    despesaRepo.delete(existente);
  }
}
