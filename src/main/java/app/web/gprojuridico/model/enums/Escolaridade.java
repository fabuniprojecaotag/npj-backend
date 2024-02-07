package app.web.gprojuridico.model.enums;

import lombok.Getter;

@Getter
public enum Escolaridade {

    FUNDAMENTAL("Fundamental"),
    MEDIO("Médio"),
    SUPERIOR("Superior"),
    POS_GRADUACAO("Pós Graduaação"),
    MESTRADO("Mestrado"),
    DOUTORADO("Doutorado");

    private final String rotulo;

    Escolaridade(String rotulo) {
        this.rotulo = rotulo;
    }
}
