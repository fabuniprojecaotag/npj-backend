package com.uniprojecao.fabrica.gprojuridico.domains.processo;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Processo {
    @DocumentId
    private String numero;
    private String atendimentoId;
    private String nome;
    private String dataDistribuicao;
    private String vara;
    private String forum;
    private String status;
    private String assistidoId;

    public Processo(String numero, String nome, String dataDistribuicao, String vara, String forum, String atendimentoId, String status, String assistidoId) {
        this.nome = nome;
        this.numero = numero;
        this.dataDistribuicao = dataDistribuicao;
        this.vara = vara;
        this.forum = forum;
        this.atendimentoId = atendimentoId;
        this.assistidoId = assistidoId;
        setStatus(status);
    }

    public void setStatus(String status) {
        this.status = Processo.Status.valueOf(status.replace(" ", "_").toUpperCase()).getValue(); // Ex. "Processo ativo" → "PROCESSO_ATIVO" → "Processo ativo". É necessária esta transformação para o armazenamento customizado de constantes.
    }

    @Getter
    enum Status {
        ATIVO("Ativo"),
        ARQUIVADO("Arquivado");

        private final String value;

        Status(String value) {
            this.value = value;
        }
    }
}
