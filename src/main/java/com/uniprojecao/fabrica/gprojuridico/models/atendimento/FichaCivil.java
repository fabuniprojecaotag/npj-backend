package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FichaCivil extends Ficha {
    private ParteContraria parteContraria;

}
