package com.db.skillup.web.mapper;

import com.db.skillup.domain.entity.Progresso;
import com.db.skillup.web.dto.ProgressoDTO;
import org.springframework.stereotype.Component;

@Component
public class ProgressoMapper {

    public ProgressoDTO toDto(Progresso progresso) {
        return new ProgressoDTO(
                progresso.getId(),
                progresso.getUsuario().getId(),
                progresso.getUsuario().getNome(),
                progresso.getCurso().getId(),
                progresso.getCurso().getNome(),
                progresso.getStatus(),
                progresso.getDataInicio(),
                progresso.getDataFim(),
                progresso.getPorcentagem()
        );
    }
}
