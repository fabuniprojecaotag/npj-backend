package com.uniprojecao.fabrica.gprojuridico.models.processo;

import com.google.cloud.firestore.annotation.DocumentId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Processo {
    @DocumentId
    @NotBlank
    private String numero;
    @NotBlank
    @Pattern(regexp = "^ATE\\d{5,}$") // exemplo[]: ["ATE00032", "ATE1234567"]
    private String atendimentoId;
    @NotBlank
    private String nome;
    @NotBlank
    private String dataDistribuicao;
    @NotBlank
    private String vara;
    @NotBlank
    private String forum;
    @NotBlank
    private String status;
    @NotBlank
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
