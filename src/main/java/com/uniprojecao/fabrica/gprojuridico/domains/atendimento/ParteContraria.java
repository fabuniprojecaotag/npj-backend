package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParteContraria {
    private String nome;
    private String qualificacao;
    private String rg;
    private String cpf;
    private String email;
    private Endereco endereco;
    private String telefone;
    private String informacoesComplementares;
}
