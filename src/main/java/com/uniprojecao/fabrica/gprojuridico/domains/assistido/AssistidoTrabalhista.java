package com.uniprojecao.fabrica.gprojuridico.domains.assistido;

import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.Filiacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssistidoTrabalhista {

    private String nome;
    private String rg;
    private String cpf;
    private String nacionalidade;
    private String escolaridade;
    private String estadoCivil;
    private String profissao;
    private String telefone;
    private String email;
    private Filiacao filiacao;
    private String remuneracao;
    private Endereco endereco;

    // dados exclusivos da ficha trabalhista
    private Ctps ctps;
    private String pis;
    private Boolean empregadoAtualmente;

}
