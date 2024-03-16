package com.uniprojecao.fabrica.gprojuridico.domains.assistido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssistidoTrabalhista extends Assistido {
    private Ctps ctps;
    private String pis;
    private Boolean empregadoAtualmente;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Ctps {
        private Integer numero;
        private Integer serie;
        private String uf;
    }
}
