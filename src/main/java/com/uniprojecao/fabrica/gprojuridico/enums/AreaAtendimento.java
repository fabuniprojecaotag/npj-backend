package com.uniprojecao.fabrica.gprojuridico.enums;

import lombok.Getter;

@Getter
public enum AreaAtendimento {
    CIVIL("Civil"),
    CRIMINAL("Criminal"),
    FAMILIA("Família"),
    TRABALHISTA("Trabalhista");

    private final String value;

    AreaAtendimento(String value) {
        this.value = value;
    }
}
