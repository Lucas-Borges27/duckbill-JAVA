package com.db.duckbill.web.mapper;

import com.db.duckbill.domain.entity.TransacaoAtivo;
import com.db.duckbill.web.dto.TransacaoAtivoDTO;

public class TransacaoAtivoMapper {
    public static TransacaoAtivoDTO toDTO(TransacaoAtivo transacao) {
        return new TransacaoAtivoDTO(
            transacao.getId(),
            transacao.getUsuario().getId(),
            transacao.getAtivo().getId(),
            transacao.getTipo(),
            transacao.getQtd(),
            transacao.getPreco(),
            transacao.getDataNegocio()
        );
    }

    public static TransacaoAtivo toEntity(TransacaoAtivoDTO dto) {
        TransacaoAtivo transacao = new TransacaoAtivo();
        transacao.setId(dto.id());
        // Note: usuario and ativo need to be set in service
        transacao.setTipo(dto.tipo());
        transacao.setQtd(dto.qtd());
        transacao.setPreco(dto.preco());
        transacao.setDataNegocio(dto.dataNegocio());
        return transacao;
    }
}
