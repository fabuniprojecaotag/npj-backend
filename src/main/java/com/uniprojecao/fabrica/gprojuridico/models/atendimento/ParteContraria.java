package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import com.uniprojecao.fabrica.gprojuridico.models.Endereco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
