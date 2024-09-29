package com.uniprojecao.fabrica.gprojuridico.models.assistido;

import com.uniprojecao.fabrica.gprojuridico.models.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssistidoCivil extends Assistido {
    @NotBlank
    private String naturalidade;

    @NotBlank
    private String dataNascimento;

    @PositiveOrZero
    private String dependentes;

    public AssistidoCivil(String nome, String rg, String cpf, String nacionalidade, String escolaridade, String estadoCivil, String profissao, String telefone, String email, Filiacao filiacao, String remuneracao, Endereco endereco, String naturalidade, String dataNascimento, String dependentes) {
        super(nome, rg, cpf, nacionalidade, escolaridade, estadoCivil, profissao, telefone, email, filiacao, remuneracao, endereco);
        this.naturalidade = naturalidade;
        this.dataNascimento = dataNascimento;
        this.dependentes = dependentes;
    }
}
