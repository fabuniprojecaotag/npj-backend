package com.uniprojecao.fabrica.gprojuridico.domains.assistido;

import com.uniprojecao.fabrica.gprojuridico.domains.Ctps;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssistidoFull extends Assistido {
    // dados exclusivos da ficha civil
    @NotBlank
    private String naturalidade;

    @NotBlank
    private String dataNascimento;

    @PositiveOrZero
    private Integer dependentes;

    // dados exclusivos da ficha trabalhista
    @NotNull
    private Ctps ctps;

    @NotBlank
    private String pis;

    @NotNull
    private Boolean empregadoAtualmente;
}
