package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.uniprojecao.fabrica.gprojuridico.models.Testemunha;
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
    private String medidaJuridica;
    private Boolean dadosSensiveis;
    private List<Testemunha> testemunhas = new ArrayList<>();

    public Ficha(String assinatura, String medidaJuridica, Boolean dadosSensiveis, List<Testemunha> testemunhas) {
        this.assinatura = assinatura;
        this.dadosSensiveis = dadosSensiveis;
        this.medidaJuridica = medidaJuridica;
        setTestemunhas(testemunhas);
    }

    public void setTestemunhas(List<Testemunha> testemunhas) {
        this.testemunhas.addAll(testemunhas);
    }
}
