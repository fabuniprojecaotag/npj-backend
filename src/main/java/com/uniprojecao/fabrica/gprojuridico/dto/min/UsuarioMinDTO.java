package com.uniprojecao.fabrica.gprojuridico.dto.min;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Estagiario.class, name = "Estagiario"),
})
public class UsuarioMinDTO {
    private String id;
    private String email;
    private String nome;
    private Boolean status;
    private String role;
}