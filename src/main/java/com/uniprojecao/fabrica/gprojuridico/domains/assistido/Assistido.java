package com.uniprojecao.fabrica.gprojuridico.domains.assistido;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Endereco endereco;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class Filiacao {
        private String mae;
        private String pai;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class Endereco {
        private String logradouro;
        private String bairro;
        private String numero;
        private String complemento;
        private String cep;
        private String cidade;

        public Endereco(String logradouro, String number) {
            this.logradouro = logradouro;
            this.numero = number;
        }
    }
}
