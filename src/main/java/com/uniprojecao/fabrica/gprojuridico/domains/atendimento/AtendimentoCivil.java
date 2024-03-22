package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AtendimentoCivil extends Atendimento {
    private FichaCivil ficha;
}
