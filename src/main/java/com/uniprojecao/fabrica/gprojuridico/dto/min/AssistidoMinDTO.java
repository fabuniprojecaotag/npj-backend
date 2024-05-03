package com.uniprojecao.fabrica.gprojuridico.dto.min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssistidoMinDTO {
    private String nome;
    private String email;
    private String cpf;
    private Quantidade quantidade;
    private String telefone;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Quantidade {
        private Integer atendimentos;
        private Integer processos;
    }
}
