package com.uniprojecao.fabrica.gprojuridico.dto.vinculados;

import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Envolvido;

public record AtendimentoVinculadoDTO(
        String id,
        String area,
        String status,
        Envolvido assistido,
        Envolvido estagiario,
        String instante
) {
}
