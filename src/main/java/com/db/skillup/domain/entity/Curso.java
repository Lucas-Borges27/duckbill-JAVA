package com.db.skillup.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SKILLUP_CURSO")
@Getter
@Setter
@NoArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CURSO")
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Size(max = 50)
    @Column(name = "CATEGORIA", length = 50)
    private String categoria;

    @Positive
    @Column(name = "CARGA_HORARIA")
    private Integer cargaHoraria;

    @Size(max = 20)
    @Column(name = "DIFICULDADE", length = 20)
    private String dificuldade;

    @Size(max = 255)
    @Column(name = "DESCRICAO", length = 255)
    private String descricao;
}
