package com.db.skillup.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "SKILLUP_PROGRESSO")
@Getter
@Setter
@NoArgsConstructor
public class Progresso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROGRESSO")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Curso curso;

    @Size(max = 20)
    @Column(name = "STATUS", length = 20)
    private String status;

    @Column(name = "DATA_INICIO")
    private LocalDate dataInicio;

    @Column(name = "DATA_FIM")
    private LocalDate dataFim;

    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    @Digits(integer = 3, fraction = 2)
    @Column(name = "PORCENTAGEM", precision = 5, scale = 2)
    private BigDecimal porcentagem;

    @PrePersist
    public void preencherDatas() {
        if (this.dataInicio == null) {
            this.dataInicio = LocalDate.now();
        }
        if (this.status == null) {
            this.status = "EM_ANDAMENTO";
        }
    }
}
