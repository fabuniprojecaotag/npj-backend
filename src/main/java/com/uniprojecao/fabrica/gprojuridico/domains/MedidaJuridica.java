package com.uniprojecao.fabrica.gprojuridico.domains;

import lombok.Getter;

@Getter
public enum MedidaJuridica {
    // FAMÍLIA
    AÇÃO_DE_ALIMENTOS("Ação de alimentos", "Família"),
    AÇÃO_DE_CUMPRIMENTO_DE_SENTENÇA_DE_ALIMENTOS_PRISÃO("Ação de cumprimento de sentença de alimentos – prisão", "Família"), // perceba que é usado "–" (travessão) ao invés do "-" (hífen)
    AÇÃO_DE_CUMPRIMENTO_DE_SENTENÇA_DE_ALIMENTOS_PENHORA("Ação de cumprimento de sentença de alimentos – penhora", "Família"), // perceba que é usado "–" (travessão) ao invés do "-" (hífen)
    AÇÃO_DE_GUARDA("Ação de guarda", "Família"),
    AÇÃO_DE_REGULAMENTAÇÃO_DE_VISITAS("Ação de regulamentação de visitas", "Família"),
    AÇÃO_DE_DIVÓRCIO("Ação de divórcio", "Família"),
    AÇÃO_DE_RECONHECIMENTO_E_DISSOLUÇÃO_DE_UNIÃO_ESTÁVEL("Ação de reconhecimento e dissolução de união estável", "Família"),
    AÇÃO_DE_RECONHECIMENTO_E_DISSOLUÇÃO_DE_UNIÃO_ESTÁVEL_POST_MORTEM("Ação de reconhecimento e dissolução de união estável post mortem", "Família"),
    AÇÃO_DE_INTERDIÇÃO("Ação de interdição", "Família"),
    AÇÃO_DE_INVENTÁRIO("Ação de inventário", "Família"),
    ALVARÁ_JUDICIAL("Alvará judicial", "Família"),

    // CIVIL
    AÇÃO_DE_REPARAÇÃO_POR_DANOS_MATERIAIS("Ação de reparação por danos materiais", "Cível"),
    AÇÃO_DE_REPARAÇÃO_POR_DANOS_MORAIS("Ação de reparação por danos morais", "Cível"),
    AÇÃO_DE_REPARAÇÃO_POR_DANOS_MATERIAIS_COM_MORAIS("Ação de reparação por danos materiais com morais", "Cível"),
    OBRIGAÇÃO_DE_FAZER("Obrigação de fazer", "Cível"),
    CONSIGNAÇÃO_EM_PAGAMENTO("Consignação em pagamento", "Cível"),
    AÇÃO_DE_COBRANÇA("Ação de cobrança", "Cível"),

    // TRABALHISTA
    RECLAMAÇÃO_TRABALHISTA("Reclamação trabalhista", "Trabalhista");

    private final String normalizedValue;
    private final String area;

    MedidaJuridica(String value, String area) {
        this.normalizedValue = value;
        this.area = area;
    }
}
