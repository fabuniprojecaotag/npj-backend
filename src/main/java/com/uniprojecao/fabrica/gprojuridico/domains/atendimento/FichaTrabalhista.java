package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FichaTrabalhista extends Ficha {
    private Reclamado reclamado;
    private RelacaoEmpregaticia relacaoEmpregaticia;
    private DocumentosDepositadosNpj documentosDepositadosNpj;
    private String outrasInformacoes;

    public FichaTrabalhista(String assinatura, Boolean dadosSensiveis, String medidaJuridica, List<Testemunha> testemunhas, Reclamado reclamado, RelacaoEmpregaticia relacaoEmpregaticia, DocumentosDepositadosNpj documentosDepositadosNpj, String outrasInformacoes) {
        super(assinatura, medidaJuridica, dadosSensiveis, testemunhas);
        this.reclamado = reclamado;
        this.relacaoEmpregaticia = relacaoEmpregaticia;
        this.documentosDepositadosNpj = documentosDepositadosNpj;
        this.outrasInformacoes = outrasInformacoes;
    }
}
