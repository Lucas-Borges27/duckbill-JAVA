package com.db.skillup.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProgressoDTO(
        Long id,
        Long usuarioId,
        String usuarioNome,
        Long cursoId,
        String cursoNome,
        String status,
        LocalDate dataInicio,
        LocalDate dataFim,
        BigDecimal porcentagem
) {}
