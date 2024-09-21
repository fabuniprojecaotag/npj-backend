package com.uniprojecao.fabrica.gprojuridico.models.assistido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class AssistidoCivil extends Assistido {
    @NotBlank
    private String naturalidade;

    @NotBlank
    private String dataNascimento;

    @PositiveOrZero
    private Integer dependentes;
}
