package com.uniprojecao.fabrica.gprojuridico.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseModel {
    @DocumentId
    private String id;
}
