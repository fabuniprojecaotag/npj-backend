package com.uniprojecao.fabrica.gprojuridico.dto.list;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Estagiario.class, name = "Estagiario"),
})
public class UsuariosListDTO {
    private String id;
    private String email;
    private String nome;
    private Boolean status;
    private String role;
}