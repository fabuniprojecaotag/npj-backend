package com.uniprojecao.fabrica.gprojuridico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessoDTO {
    private String numero;
    private String nome;
    private String data;
    private String vara;
    private String forum;
    private String atendimento;
}
