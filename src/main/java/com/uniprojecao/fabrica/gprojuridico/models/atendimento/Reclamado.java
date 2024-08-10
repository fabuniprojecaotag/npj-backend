package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import com.uniprojecao.fabrica.gprojuridico.models.Endereco;
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
    private Endereco endereco;
}
