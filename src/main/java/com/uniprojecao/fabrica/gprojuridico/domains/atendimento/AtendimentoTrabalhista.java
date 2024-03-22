package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AtendimentoTrabalhista extends Atendimento {
    private FichaTrabalhista ficha;
}
