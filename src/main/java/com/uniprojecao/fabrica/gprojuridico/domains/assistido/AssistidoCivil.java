package com.uniprojecao.fabrica.gprojuridico.domains.assistido;

import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;
import com.uniprojecao.fabrica.gprojuridico.domains.Filiacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class AssistidoCivil extends Assistido {
    @NotBlank
    private String naturalidade;

    @NotBlank
    private String dataNascimento;

    @PositiveOrZero
    private Integer dependentes;

    public AssistidoCivil(String nome, String rg, String cpf, String nacionalidade, String escolaridade, String estadoCivil, String profissao, String telefone, String email, Filiacao filiacao, String remuneracao, Map<String, Endereco> endereco, String naturalidade, String dataNascimento, Integer dependentes) {
        super(nome, rg, cpf, nacionalidade, escolaridade, estadoCivil, profissao, telefone, email, filiacao, remuneracao, endereco);
        this.naturalidade = naturalidade;
        this.dataNascimento = dataNascimento;
        this.dependentes = dependentes;
    }
}
