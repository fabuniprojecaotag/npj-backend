package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FichaCivil.class, name = "Civil"),
        @JsonSubTypes.Type(value = FichaTrabalhista.class, name = "Trabalhista")
})
public abstract class Ficha {
    private String assinatura;
    private Boolean dadosSensiveis;
    private List<Testemunha> testemunhas = new ArrayList<>();

    public Ficha(String assinatura, Boolean dadosSensiveis, List<Testemunha> testemunhas) {
        this.assinatura = assinatura;
        this.dadosSensiveis = dadosSensiveis;
        setTestemunhas(testemunhas);
    }

    public void setTestemunhas(List<Testemunha> testemunhas) {
        this.testemunhas.addAll(testemunhas);
    }

    public void removeTestemunha(Testemunha testemunha) {
        testemunhas.remove(testemunha);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Testemunha {
        private String nome;
        private String qualificacao;
        private Endereco endereco;
    }
}
