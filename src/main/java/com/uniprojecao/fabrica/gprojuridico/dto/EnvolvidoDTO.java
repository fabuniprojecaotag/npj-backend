package com.uniprojecao.fabrica.gprojuridico.dto;

import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EnvolvidoDTO {
    private String id;
    private String nome;
    private Endereco endereco;
}
