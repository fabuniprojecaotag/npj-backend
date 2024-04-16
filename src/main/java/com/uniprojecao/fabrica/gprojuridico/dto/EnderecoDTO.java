package com.uniprojecao.fabrica.gprojuridico.dto;

import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EnderecoDTO {
    @NotBlank
    private String logradouro;
    private String bairro;
    private String numero;
    private String complemento;

    @Pattern(regexp = "^\\d{5}-\\d{3}$") // exemplo: 01001-000
    private String cep;
    private String cidade;

    public EnderecoDTO(String logradouro, String number) {
        this.logradouro = logradouro;
        this.numero = number;
    }

    public EnderecoDTO(Endereco endereco) {
        logradouro = endereco.getLogradouro();
        numero = endereco.getNumero();
    }
}
