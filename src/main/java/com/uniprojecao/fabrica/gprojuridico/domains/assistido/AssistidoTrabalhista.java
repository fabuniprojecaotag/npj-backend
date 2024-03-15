package com.uniprojecao.fabrica.gprojuridico.domains.assistido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssistidoTrabalhista extends Assistido {
    @NotNull
    private Ctps ctps;

    @NotBlank
    private String pis;

    @NotNull
    private Boolean empregadoAtualmente;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class Ctps {
        @Positive
        private Integer numero;

        @Positive
        private Integer serie;

        @NotBlank
        @Size(min = 2, max = 2)
        private String uf;
    }
}
