package com.uniprojecao.fabrica.gprojuridico.dto.atendimento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AtendimentoTrabalhistaDTO extends AtendimentoDTO {
    private FichaTrabalhistaDTO ficha;
}
