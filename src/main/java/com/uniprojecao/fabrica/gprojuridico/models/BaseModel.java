package com.uniprojecao.fabrica.gprojuridico.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseModel {
    @DocumentId
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
}
