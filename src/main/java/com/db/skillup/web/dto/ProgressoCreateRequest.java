package com.db.skillup.web.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProgressoCreateRequest(
        @NotNull(message = "usuarioId é obrigatório")
        Long usuarioId,
        @NotNull(message = "cursoId é obrigatório")
        Long cursoId,
        @Size(max = 20)
        String status,
        LocalDate dataInicio,
        LocalDate dataFim,
        @DecimalMin(value = "0.00")
        @DecimalMax(value = "100.00")
        @Digits(integer = 3, fraction = 2)
        BigDecimal porcentagem
) {}
