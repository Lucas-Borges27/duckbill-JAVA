package com.db.duckbill.service;

import com.db.duckbill.domain.entity.TransacaoAtivo;
import com.db.duckbill.domain.repo.AtivoRepository;
import com.db.duckbill.domain.repo.TransacaoAtivoRepository;
import com.db.duckbill.domain.repo.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransacaoAtivoService {
    private final TransacaoAtivoRepository transacaoAtivoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AtivoRepository ativoRepository;

    @Transactional
    public TransacaoAtivo criar(com.db.duckbill.web.dto.TransacaoAtivoDTO dto) {
        var usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        var ativo = ativoRepository.findById(dto.ativoId())
                .orElseThrow(() -> new NoSuchElementException("Ativo não encontrado"));

        TransacaoAtivo transacao = new TransacaoAtivo();
        transacao.setUsuario(usuario);
        transacao.setAtivo(ativo);
        transacao.setTipo(dto.tipo().toUpperCase());
        transacao.setQtd(dto.qtd());
        transacao.setPreco(dto.preco());
        transacao.setDataNegocio(dto.dataNegocio());

        return transacaoAtivoRepository.save(transacao);
    }

    public List<TransacaoAtivo> listar() {
        return transacaoAtivoRepository.findAll();
    }

    public TransacaoAtivo buscarPorId(Long id) {
        return transacaoAtivoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Transação não encontrada"));
    }

    @Transactional
    public TransacaoAtivo atualizar(Long id, TransacaoAtivo transacao) {
        TransacaoAtivo existente = buscarPorId(id);
        existente.setUsuario(transacao.getUsuario());
        existente.setAtivo(transacao.getAtivo());
        existente.setTipo(transacao.getTipo());
        existente.setQtd(transacao.getQtd());
        existente.setPreco(transacao.getPreco());
        existente.setDataNegocio(transacao.getDataNegocio());
        return transacaoAtivoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        transacaoAtivoRepository.deleteById(id);
    }
}
