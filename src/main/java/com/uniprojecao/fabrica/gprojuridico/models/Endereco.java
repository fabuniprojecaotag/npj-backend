package com.uniprojecao.fabrica.gprojuridico.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Endereco {
    @NotBlank
    private String logradouro;
    private String bairro;
    private String numero;
    private String complemento;

    @Pattern(regexp = "^\\d{5}-\\d{3}$") // exemplo: 01001-000
    private String cep;
    private String cidade;
}
