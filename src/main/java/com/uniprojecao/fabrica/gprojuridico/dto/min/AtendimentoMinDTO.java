package com.uniprojecao.fabrica.gprojuridico.dto.min;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String assistido;
    private String dataCriacao;
}
