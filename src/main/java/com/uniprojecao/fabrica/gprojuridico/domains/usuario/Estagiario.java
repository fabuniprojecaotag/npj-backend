package com.uniprojecao.fabrica.gprojuridico.domains.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Estagiario extends Usuario {
    private String matricula;
    private String semestre;
    private SupervisorMin supervisor;
}
