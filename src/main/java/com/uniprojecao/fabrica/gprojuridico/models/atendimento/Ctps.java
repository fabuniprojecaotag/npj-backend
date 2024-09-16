package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ctps {
    @NotBlank
    private String numero;

    @NotBlank
    private String serie;

    @NotBlank
    @Size(min = 2, max = 2)
    private String uf;
}
