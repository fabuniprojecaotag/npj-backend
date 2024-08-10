package com.uniprojecao.fabrica.gprojuridico.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ctps {
    @NotBlank
    private String numero;

    @NotBlank
    private String serie;

    @NotBlank
    @Size(min = 2, max = 2)
    private String uf;
}
