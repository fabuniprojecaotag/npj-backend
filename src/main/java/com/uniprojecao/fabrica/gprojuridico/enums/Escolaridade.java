package com.uniprojecao.fabrica.gprojuridico.enums;

import lombok.Getter;

@Getter
enum Escolaridade {
    FUNDAMENTAL("Fundamental"),
    MEDIO("Médio"),
    SUPERIOR("Superior"),
    POS_GRADUACAO("Pós Graduação"),
    MESTRADO("Mestrado"),
    DOUTORADO("Doutorado");

    private final String value;

    Escolaridade(String value) {
        this.value = value;
    }
}
