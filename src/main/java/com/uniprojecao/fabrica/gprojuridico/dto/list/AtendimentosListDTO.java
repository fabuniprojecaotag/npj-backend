package com.uniprojecao.fabrica.gprojuridico.dto.list;

import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Envolvido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AtendimentosListDTO {
    private String id;
    private String area;
    private String status;
    private Envolvido assistido;
    private String instante;
}
