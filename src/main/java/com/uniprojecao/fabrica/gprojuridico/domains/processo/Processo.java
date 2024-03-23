package com.uniprojecao.fabrica.gprojuridico.domains.processo;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Processo {
    @DocumentId
    private String numero;
    private String nome;
    private String dataDistribuicao;
    private String vara;
    private String forum;
    private String atendimentoId;
}
