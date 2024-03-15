package com.uniprojecao.fabrica.gprojuridico.domains.assistido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssistidoCivil extends Assistido {
    @NotBlank
    private String naturalidade;

    @NotBlank
    private String dataNascimento;

    @NotBlank
    @PositiveOrZero
    private Integer dependentes;
}
