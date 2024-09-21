package com.uniprojecao.fabrica.gprojuridico.models.assistido;

import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Ctps;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssistidoTrabalhista extends Assistido {
    @NotNull
    private Ctps ctps;

    @NotBlank
    private String pis;

    @NotNull
    private Boolean empregadoAtualmente;

}
