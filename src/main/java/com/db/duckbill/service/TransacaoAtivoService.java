package com.db.duckbill.service;

import com.db.duckbill.domain.entity.TransacaoAtivo;
import com.db.duckbill.domain.repo.AtivoRepository;
import com.db.duckbill.domain.repo.TransacaoAtivoRepository;
import com.db.duckbill.domain.repo.UsuarioRepository;
import com.db.duckbill.web.dto.CarteiraResumoDTO;
import com.db.duckbill.web.dto.TransacaoAtivoDTO;
import com.db.duckbill.web.exception.AcessoNegadoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransacaoAtivoService {
    private final TransacaoAtivoRepository transacaoAtivoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AtivoRepository ativoRepository;

    @Transactional
    public TransacaoAtivo criar(TransacaoAtivoDTO dto) {
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

    public List<TransacaoAtivo> listarPorUsuario(Long usuarioId) {
        return transacaoAtivoRepository.findByUsuario_IdOrderByDataNegocioDesc(usuarioId);
    }

    public TransacaoAtivo buscarPorId(Long id) {
        return transacaoAtivoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Transação não encontrada"));
    }

    public TransacaoAtivo buscarPorIdAutorizado(Long id, Long usuarioId, boolean admin) {
        TransacaoAtivo transacao = buscarPorId(id);
        if (!admin && !Objects.equals(transacao.getUsuario().getId(), usuarioId)) {
            throw new AcessoNegadoException("Acesso negado à transação.");
        }
        return transacao;
    }

    @Transactional
    public TransacaoAtivo atualizar(Long id, TransacaoAtivoDTO dto, Long usuarioId, boolean admin) {
        TransacaoAtivo existente = buscarPorIdAutorizado(id, usuarioId, admin);
        var usuario = usuarioRepository.findById(dto.usuarioId())
            .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        var ativo = ativoRepository.findById(dto.ativoId())
            .orElseThrow(() -> new NoSuchElementException("Ativo não encontrado"));

        existente.setUsuario(usuario);
        existente.setAtivo(ativo);
        existente.setTipo(dto.tipo().toUpperCase());
        existente.setQtd(dto.qtd());
        existente.setPreco(dto.preco());
        existente.setDataNegocio(dto.dataNegocio());
        return transacaoAtivoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id, Long usuarioId, boolean admin) {
        TransacaoAtivo existente = buscarPorIdAutorizado(id, usuarioId, admin);
        transacaoAtivoRepository.delete(existente);
    }

    public List<CarteiraResumoDTO> resumoCarteira(Long usuarioId) {
        return listarPorUsuario(usuarioId).stream()
            .collect(java.util.stream.Collectors.groupingBy(
                t -> t.getAtivo().getTicker(),
                java.util.stream.Collectors.toList()
            ))
            .entrySet().stream()
            .map(entry -> {
                BigDecimal quantidadeAtual = entry.getValue().stream()
                    .map(t -> "BUY".equalsIgnoreCase(t.getTipo()) ? t.getQtd() : t.getQtd().negate())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal financeiroLiquido = entry.getValue().stream()
                    .map(t -> t.getQtd().multiply(t.getPreco()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                return new CarteiraResumoDTO(entry.getKey(), quantidadeAtual, financeiroLiquido);
            })
            .sorted(java.util.Comparator.comparing(CarteiraResumoDTO::ticker))
            .toList();
    }
}
