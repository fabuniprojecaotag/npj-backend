package app.web.gprojuridico.domains.enums;

import lombok.Getter;

@Getter
public enum Escolaridade {

    FUNDAMENTAL("Fundamental"),
    MEDIO("Médio"),
    SUPERIOR("Superior"),
    POS_GRADUACAO("Pós Graduação"),
    MESTRADO("Mestrado"),
    DOUTORADO("Doutorado");

    private final String rotulo;

    Escolaridade(String rotulo) {
        this.rotulo = rotulo;
    }
}
