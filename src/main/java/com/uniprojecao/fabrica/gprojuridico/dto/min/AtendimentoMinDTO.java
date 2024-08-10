package com.uniprojecao.fabrica.gprojuridico.dto.min;

import com.uniprojecao.fabrica.gprojuridico.domains.Envolvido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AtendimentoMinDTO {
    private String id;
    private String area;
    private String status;
    private Envolvido assistido;
    private String instante;
}
