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
    @Pattern(regexp = "^\\d{9}$")
    private String matricula;

    @Pattern(regexp = "^(8|9|10)$")
    private String semestre;

    @NotNull
    private transient SupervisorMin supervisor;
}
