package com.db.duckbill.service;

import com.db.duckbill.domain.entity.Ativo;
import com.db.duckbill.domain.repo.AtivoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AtivoService {
    private final AtivoRepository ativoRepository;

    public AtivoService(AtivoRepository ativoRepository) {
        this.ativoRepository = ativoRepository;
    }

    @Transactional
    public Ativo criar(Ativo ativo) {
        ativo.setMoedaBase(Optional.ofNullable(ativo.getMoedaBase()).orElse("BRL").toUpperCase());
        return ativoRepository.save(ativo);
    }

    public List<Ativo> listar() {
        return ativoRepository.findAll();
    }

    public Ativo buscarPorId(Long id) {
        return ativoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Ativo não encontrado"));
    }

    @Transactional
    public Ativo atualizar(Long id, Ativo ativo) {
        Ativo existente = buscarPorId(id);
        existente.setTicker(ativo.getTicker());
        existente.setTipo(ativo.getTipo());
        existente.setMoedaBase(ativo.getMoedaBase());
        return ativoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        ativoRepository.deleteById(id);
    }
}
