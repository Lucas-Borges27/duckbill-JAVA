package com.db.skillup.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "SKILLUP_USUARIO")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "EMAIL", unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    @Column(name = "SENHA", nullable = false, length = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @Size(max = 50)
    @Column(name = "AREA_INTERESSE", length = 50)
    private String areaInteresse;

    @Column(name = "DATA_CADASTRO")
    private LocalDate dataCadastro;

    @PrePersist
    public void preencherDataCadastro() {
        if (this.dataCadastro == null) {
            this.dataCadastro = LocalDate.now();
        }
    }
}
