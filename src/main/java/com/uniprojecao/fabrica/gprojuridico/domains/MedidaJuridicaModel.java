package com.uniprojecao.fabrica.gprojuridico.domains;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedidaJuridicaModel {
    @DocumentId
    private String nome;
    private String descricao;
    private String area;
}
