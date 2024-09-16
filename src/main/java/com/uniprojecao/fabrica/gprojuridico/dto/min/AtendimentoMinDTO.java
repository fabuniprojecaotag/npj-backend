package com.uniprojecao.fabrica.gprojuridico.dto.min;

import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Envolvido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AtendimentoMinDTO {
    private String id;
    private String area;
    private String status;
    private Envolvido assistido;
    private String instante;
}
