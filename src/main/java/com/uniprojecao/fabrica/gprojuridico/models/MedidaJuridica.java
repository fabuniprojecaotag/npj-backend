package com.uniprojecao.fabrica.gprojuridico.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedidaJuridica {
    @DocumentId
    private String id;
    private String nome;
    private String descricao;
    private String area;
}
