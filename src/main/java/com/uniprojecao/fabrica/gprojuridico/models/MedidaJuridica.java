package com.uniprojecao.fabrica.gprojuridico.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MedidaJuridica extends BaseModel{
    private String nome;
    private String descricao;
    private String area;
}
