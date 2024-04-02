package com.uniprojecao.fabrica.gprojuridico.dto.min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AtendimentoMinDTO {
    private String id;
    private String area;
    private String status;
    private String assistido; // TODO: Mudar tipo para AssistidoMin
    private String dataCriacao;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class AssistidoMin {
        private String id;
        private String nome;
    }
}
