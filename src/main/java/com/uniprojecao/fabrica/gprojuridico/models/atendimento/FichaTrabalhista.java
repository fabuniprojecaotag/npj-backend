package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FichaTrabalhista extends Ficha {
    private Reclamado reclamado;
    private RelacaoEmpregaticia relacaoEmpregaticia;
    private DocumentosDepositadosNpj documentosDepositadosNpj;
    private String outrasInformacoes;
}
