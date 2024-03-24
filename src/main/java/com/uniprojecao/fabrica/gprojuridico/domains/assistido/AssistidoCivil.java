package com.uniprojecao.fabrica.gprojuridico.domains.assistido;

import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AssistidoCivil extends Assistido {
    private String naturalidade;
    private String dataNascimento;
    private Integer dependentes;

    public AssistidoCivil(String nome, String rg, String cpf, String nacionalidade, String escolaridade, String estadoCivil, String profissao, String telefone, String email, Filiacao filiacao, String remuneracao, Endereco endereco, String naturalidade, String dataNascimento, Integer dependentes) {
        super(nome, rg, cpf, nacionalidade, escolaridade, estadoCivil, profissao, telefone, email, filiacao, remuneracao, endereco);
        this.naturalidade = naturalidade;
        this.dataNascimento = dataNascimento;
        this.dependentes = dependentes;
    }
}
