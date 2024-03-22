package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichaTrabalhista extends Ficha {
    private Reclamado reclamado;
    private RelacaoEmpregaticia relacaoEmpregaticia;
    private DocumentosDepositadosNpj documentosDepositadosNpj;
    private String outrasInformacoes;
}
