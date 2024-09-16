package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
