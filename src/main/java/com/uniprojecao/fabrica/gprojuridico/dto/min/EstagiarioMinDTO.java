package com.uniprojecao.fabrica.gprojuridico.dto.min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EstagiarioMinDTO extends UsuarioMinDTO {
    private String matricula;
    private String semestre;
}
