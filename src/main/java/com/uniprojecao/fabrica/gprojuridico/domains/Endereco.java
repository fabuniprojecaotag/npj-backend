package com.uniprojecao.fabrica.gprojuridico.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Endereco {
    private String logradouro;
    private String bairro;
    private String numero;
    private String complemento;
    private String cep;
    private String cidade;

    public Endereco(String logradouro, String number) {
        this.logradouro = logradouro;
        this.numero = number;
    }
}
