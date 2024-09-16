package com.uniprojecao.fabrica.gprojuridico.models.usuario;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Estagiario extends Usuario {
    @Pattern(regexp = "^[0-9]{9}$")
    private String matricula;

    @Pattern(regexp = "^(8|9|10)$")
    private String semestre;

    @NotNull
    private SupervisorMin supervisor;

    public Estagiario(
            String id,
            String email,
            String nome,
            String cpf,
            String unidadeInstitucional,
            String senha,
            Boolean status,
            String role,
            String matricula,
            String semestre,
            SupervisorMin supervisor
    ) {
        super(id, email, nome, cpf, unidadeInstitucional, senha, status, role);
        this.matricula = matricula;
        this.semestre = semestre;
        this.supervisor = supervisor;
    }
}
