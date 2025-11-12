package com.db.duckbill.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoAtivoDTO(
    Long id,
    Long usuarioId,
    Long ativoId,
    String tipo,
    BigDecimal qtd,
    BigDecimal preco,
    LocalDate dataNegocio
) {}
