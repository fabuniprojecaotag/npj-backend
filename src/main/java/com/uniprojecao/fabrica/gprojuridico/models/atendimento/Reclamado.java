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
public class Reclamado {
    private String nome;
    private String tipoPessoa;
    private String numCadastro;
    private Endereco endereco;
}
