package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reclamado {
    private String nome;
    private String tipoPessoa;
    private String numCadastro;
    private Endereco endereco;
}
