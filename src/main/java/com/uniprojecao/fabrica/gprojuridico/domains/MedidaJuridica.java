package com.uniprojecao.fabrica.gprojuridico.domains;

import lombok.Getter;

@Getter
public enum MedidaJuridica {
    // FAMÍLIA
    AÇÃO_DE_ALIMENTOS("Ação de alimentos"),
    AÇÃO_DE_CUMPRIMENTO_DE_SENTENÇA_DE_ALIMENTOS_PRISÃO("Ação de cumprimento de sentença de alimentos – prisão"), // perceba que é usado "–" (travessão) ao invés do "-" (hífen)
    AÇÃO_DE_CUMPRIMENTO_DE_SENTENÇA_DE_ALIMENTOS_PENHORA("Ação de cumprimento de sentença de alimentos – penhora"), // perceba que é usado "–" (travessão) ao invés do "-" (hífen)
    AÇÃO_DE_GUARDA("Ação de guarda"),
    AÇÃO_DE_REGULAMENTAÇÃO_DE_VISITAS("Ação de regulamentação de visitas"),
    AÇÃO_DE_DIVÓRCIO("Ação de divórcio"),
    AÇÃO_DE_RECONHECIMENTO_E_DISSOLUÇÃO_DE_UNIÃO_ESTÁVEL("Ação de reconhecimento e dissolução de união estável"),
    AÇÃO_DE_RECONHECIMENTO_E_DISSOLUÇÃO_DE_UNIÃO_ESTÁVEL_POST_MORTEM("Ação de reconhecimento e dissolução de união estável post mortem"),
    AÇÃO_DE_INTERDIÇÃO("Ação de interdição"),
    AÇÃO_DE_INVENTÁRIO("Ação de inventário"),
    ALVARÁ_JUDICIAL("Alvará judicial"),

    // CIVIL
    AÇÃO_DE_REPARAÇÃO_POR_DANOS_MATERIAIS("Ação de reparação por danos materiais"),
    AÇÃO_DE_REPARAÇÃO_POR_DANOS_MORAIS("Ação de reparação por danos morais"),
    AÇÃO_DE_REPARAÇÃO_POR_DANOS_MATERIAIS_COM_MORAIS("Ação de reparação por danos materiais com morais"),
    OBRIGAÇÃO_DE_FAZER("Obrigação de fazer"),
    CONSIGNAÇÃO_EM_PAGAMENTO("Consignação em pagamento"),
    AÇÃO_DE_COBRANÇA("Ação de cobrança"),

    // TRABALHISTA
    RECLAMAÇÃO_TRABALHISTA("Reclamação trabalhista");

    private final String normalizedValue;

    MedidaJuridica(String value) {
        this.normalizedValue = value;
    }
}
