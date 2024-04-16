package com.uniprojecao.fabrica.gprojuridico.domains.assistido;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.cloud.firestore.annotation.DocumentId;

import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AssistidoCivil.class, name = "Civil"),
        @JsonSubTypes.Type(value = AssistidoTrabalhista.class, name = "Trabalhista"),
        @JsonSubTypes.Type(value = AssistidoFull.class, name = "Full"),
})
public abstract class Assistido {
    private String nome;
    private String rg;
    @DocumentId
    private String cpf;
    private String nacionalidade;
    private String escolaridade;
    private String estadoCivil;
    private String profissao;
    private String telefone;
    private String email;
    private Filiacao filiacao;
    private String remuneracao;
    private Map<String, Endereco> endereco;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Filiacao {
        private String mae;
        private String pai;
    }
}
