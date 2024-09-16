package com.uniprojecao.fabrica.gprojuridico.dto.min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EstagiarioMinDTO extends UsuarioMinDTO {
    private String matricula;
    private String semestre;
}
