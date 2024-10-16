package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DocumentosDepositadosNpj {
    private Boolean procuracao;
    private Boolean declaracaoPobreza;
    private Boolean ctps;
    private Boolean identidade;
    private Boolean cpf;
    private Boolean pis;
    private Boolean contrachequeUltimos3Meses;
    private Boolean extratoAnaliticoContaFgts;
    private Boolean trct;
    private Boolean comprovanteRecAntecip13;
    private Boolean acordoColetivoTrabalho;
    private String outrosDocumentos;
}
