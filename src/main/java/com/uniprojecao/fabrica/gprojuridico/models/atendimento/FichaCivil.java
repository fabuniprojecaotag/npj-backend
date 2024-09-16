package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FichaCivil extends Ficha {
    private ParteContraria parteContraria;

    public FichaCivil(
            String assinatura,
            Boolean dadosSensiveis,
            List<Testemunha> testemunhas,
            ParteContraria parteContraria,
            String medidaJuridica
    ) {
        super(assinatura, medidaJuridica, dadosSensiveis, testemunhas);
        this.parteContraria = parteContraria;
    }
}
