package com.uniprojecao.fabrica.gprojuridico.dto.min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssistidoMinDTO {
    private String nome;
    private String email;
    private String cpf;
    private Quantidade quantidade;
    private String telefone;

    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Quantidade {
        private Integer atendimentos;
        private Integer processos;
    }
}
