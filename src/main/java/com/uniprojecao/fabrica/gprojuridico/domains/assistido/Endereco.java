package com.uniprojecao.fabrica.gprojuridico.domains.assistido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
