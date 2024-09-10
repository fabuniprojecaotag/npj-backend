package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class AtendimentoCivil extends Atendimento {
    private FichaCivil ficha;

    public AtendimentoCivil(
            String id,
            String status,
            String area,
            String instante,
            String historico,
            Map<String, Envolvido> envolvidos,
            FichaCivil ficha
    ) {
        super(id, status, area, instante, historico, envolvidos);
        this.ficha = ficha;
    }
}
