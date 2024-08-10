package com.uniprojecao.fabrica.gprojuridico.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Testemunha {
    @NotBlank
    private String nome;
    private String qualificacao;
    private Endereco endereco;
}
