package com.db.duckbill.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CotacaoAtivoDTO(
    Long ativoId,
    LocalDate dataRef,
    BigDecimal precoFech
) {}
