package com.db.duckbill.web.dto;

public record AtivoDTO(
    Long id,
    String ticker,
    String tipo,
    String moedaBase
) {}
