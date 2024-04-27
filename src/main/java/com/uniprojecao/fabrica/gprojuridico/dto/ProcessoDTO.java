package com.uniprojecao.fabrica.gprojuridico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessoDTO {
    @NotBlank
    private String numero;
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

    public void setStatus(String status) {
        this.status = ProcessoDTO.Status.valueOf(status.replace(" ", "_").toUpperCase()).getValue(); // Ex. "Processo ativo" → "PROCESSO_ATIVO" → "Processo ativo". É necessária esta transformação para o armazenamento customizado de constantes.
    }

    @Getter
    enum Status {
        REPROVADO("Reprovado"),
        ARQUIVADO("Arquivado"),
        AGUARDANDO_DOCUMENTOS("Aguardando documentos"),
        PENDENTE_DISTRIBUICAO("Pendente distribuição"),
        PROCESSO_ATIVO("Processo ativo"),
        PROCESSO_ARQUIVADO("Processo arquivado");

        private final String value;

        Status(String value) {
            this.value = value;
        }
    }
}
