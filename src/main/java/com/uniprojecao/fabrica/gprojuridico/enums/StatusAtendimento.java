package com.uniprojecao.fabrica.gprojuridico.enums;

import lombok.Getter;

@Getter
public enum StatusAtendimento {
    REPROVADO("Reprovado"),
    ARQUIVADO("Arquivado"),
    AGUARDANDO_DOCUMENTOS("Aguardando documentos"),
    PENDENTE_DISTRIBUICAO("Pendente distribuição"),
    PROCESSO_ATIVO("Processo ativo"),
    PROCESSO_ARQUIVADO("Processo arquivado");

    private final String value;

    StatusAtendimento(String value) {
        this.value = value;
    }
}
