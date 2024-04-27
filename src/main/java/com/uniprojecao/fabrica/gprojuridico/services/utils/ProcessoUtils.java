package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.uniprojecao.fabrica.gprojuridico.domains.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.dto.ProcessoDTO;

public class ProcessoUtils {
    public static Processo dtoToProcesso(ProcessoDTO dto) {
        return new Processo(
                dto.getNumero(),
                dto.getAtendimentoId(),
                dto.getNome(),
                dto.getDataDistribuicao(),
                dto.getVara(),
                dto.getForum(),
                dto.getStatus()
        );
    }

    public static ProcessoDTO processoToDto(Processo entity) {
        return new ProcessoDTO(
                entity.getNumero(),
                entity.getAtendimentoId(),
                entity.getNome(),
                entity.getDataDistribuicao(),
                entity.getVara(),
                entity.getForum(),
                entity.getStatus()
        );
    }
}
