package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FichaCivil extends Ficha {
    private ParteContraria parteContraria;
    private String medidaJudicial;

    public FichaCivil(String assinatura, Boolean dadosSensiveis, List<Testemunha> testemunhas, ParteContraria parteContraria, String medidaJudicial) {
        super(assinatura, dadosSensiveis, testemunhas);
        this.parteContraria = parteContraria;
        this.medidaJudicial = medidaJudicial;
    }
}
