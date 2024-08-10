package com.uniprojecao.fabrica.gprojuridico.enums;

import lombok.Getter;

@Getter
enum EstadoCivil {
    SOLTEIRO("Solteiro"),
    SOLTEIRA("Solteira"),
    CASADO("Casado"),
    CASADA("Casada"),
    SEPARADO("Separado"),
    SEPARADA("Separada"),
    DIVORCIADO("Divorciado"),
    DIVORCIADA("Divorciada"),
    VIUVO("Viúvo"),
    VIUVA("Viúva");

    private final String value;

    EstadoCivil(String value) {
        this.value = value;
    }
}
