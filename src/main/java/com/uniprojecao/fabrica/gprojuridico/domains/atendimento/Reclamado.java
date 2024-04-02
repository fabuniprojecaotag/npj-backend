package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reclamado {
    private String nome;
    private String tipoPessoa;
    private String numCadastro;
    private String endereco;
}
