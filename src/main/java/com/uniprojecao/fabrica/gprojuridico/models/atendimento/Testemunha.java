package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import com.uniprojecao.fabrica.gprojuridico.models.Endereco;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Testemunha {
    @NotBlank
    private String nome;
    private String qualificacao;
    private Endereco endereco;
}
