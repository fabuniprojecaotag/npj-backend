package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import com.uniprojecao.fabrica.gprojuridico.dto.EnvolvidoDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data
public class AtendimentoTrabalhista extends Atendimento {
    private FichaTrabalhista ficha;

    public AtendimentoTrabalhista(String id, String status, String area, String instante, List<EntradaHistorico> historico, Map<String, EnvolvidoDTO> envolvidos, FichaTrabalhista ficha) {
        super(id, status, area, instante, historico, envolvidos);
        this.ficha = ficha;
    }
}
