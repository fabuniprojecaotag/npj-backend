package com.uniprojecao.fabrica.gprojuridico.domains.enums;

import lombok.Getter;

@Getter
public enum EstadoCivil {

    SOLTEIRO("Solteiro"),
    CASADO("Casado"),
    SEPARADO("Separado"),
    DIVORCIADO("Divorciado"),
    VIUVO("Viúvo");

    private final String rotulo;

    EstadoCivil(String rotulo) {
        this.rotulo = rotulo;
    }
}
