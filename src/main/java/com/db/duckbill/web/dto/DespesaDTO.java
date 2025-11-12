package com.db.duckbill.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaDTO(
    Long id,
    Long usuarioId,
    Long categoriaId,
    BigDecimal valor,
    String moeda,
    LocalDate dataCompra,
    String descricao
) {}
