package com.uniprojecao.fabrica.gprojuridico.domains.assistido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssistidoCivil extends Assistido {
    private String naturalidade;
    private String dataNascimento;
    private Integer dependentes;
}
