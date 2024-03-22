package com.uniprojecao.fabrica.gprojuridico.dto.atendimento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AtendimentoCivilDTO extends AtendimentoDTO {
    private FichaCivilDTO ficha;
}
