package com.uniprojecao.fabrica.gprojuridico.dto.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EstagiariosListDTO extends UsuariosListDTO {
    private String matricula;
    private String semestre;
}
