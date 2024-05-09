package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FichaCivil extends Ficha {
    private ParteContraria parteContraria;

    public FichaCivil(String assinatura, Boolean dadosSensiveis, List<Testemunha> testemunhas, ParteContraria parteContraria, String medidaJuridica) {
        super(assinatura, medidaJuridica, dadosSensiveis, testemunhas);
        this.parteContraria = parteContraria;
    }
}
