package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import com.google.cloud.Timestamp;
import com.uniprojecao.fabrica.gprojuridico.dto.EnvolvidoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data
public class AtendimentoCivil extends Atendimento {
    private FichaCivil ficha;

    public AtendimentoCivil(String id, String status, String area, Timestamp instante, Timestamp prazo, List<EntradaHistorico> historico, Map<String, EnvolvidoDTO> envolvidos, FichaCivil ficha) {
        super(id, status, area, instante, prazo, historico, envolvidos);
        this.ficha = ficha;
    }
}
