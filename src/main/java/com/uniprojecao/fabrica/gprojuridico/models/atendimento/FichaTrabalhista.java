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
public class FichaTrabalhista extends Ficha {
    private Reclamado reclamado;
    private RelacaoEmpregaticia relacaoEmpregaticia;
    private DocumentosDepositadosNpj documentosDepositadosNpj;
    private String outrasInformacoes;

    public FichaTrabalhista(
            String assinatura,
            Boolean dadosSensiveis,
            String medidaJuridica,
            List<Testemunha> testemunhas,
            Reclamado reclamado,
            RelacaoEmpregaticia relacaoEmpregaticia,
            DocumentosDepositadosNpj documentosDepositadosNpj,
            String outrasInformacoes
    ) {
        super(assinatura, medidaJuridica, dadosSensiveis, testemunhas);
        this.reclamado = reclamado;
        this.relacaoEmpregaticia = relacaoEmpregaticia;
        this.documentosDepositadosNpj = documentosDepositadosNpj;
        this.outrasInformacoes = outrasInformacoes;
    }
}
