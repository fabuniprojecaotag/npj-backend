package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class AtendimentoTrabalhista extends Atendimento {
    private FichaTrabalhista ficha;

    public AtendimentoTrabalhista(
            String id,
            String status,
            String area,
            String instante,
            String historico,
            Map<String, Envolvido> envolvidos,
            FichaTrabalhista ficha
    ) {
        super(id, status, area, instante, historico, envolvidos);
        this.ficha = ficha;
    }
}
