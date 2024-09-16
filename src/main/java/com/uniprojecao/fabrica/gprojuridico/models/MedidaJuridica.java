package com.uniprojecao.fabrica.gprojuridico.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MedidaJuridica {
    @DocumentId
    private String id;
    private String nome;
    private String descricao;
    private String area;
}
