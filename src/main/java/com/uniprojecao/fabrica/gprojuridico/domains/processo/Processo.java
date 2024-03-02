package com.uniprojecao.fabrica.gprojuridico.domains.processo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Processo {
    private Integer numero;
    private String nome;
    private String dataDistribuicao;
    private String vara;
    private String forum;
    private String atendimentoId;
}
