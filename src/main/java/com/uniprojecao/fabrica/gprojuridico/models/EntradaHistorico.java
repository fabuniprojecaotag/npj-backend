package com.uniprojecao.fabrica.gprojuridico.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoField;

@NoArgsConstructor
@Data
public class EntradaHistorico {
    @DocumentId
    private String id;
    private String titulo;
    private String descricao;

    @JsonIgnore
    private String instante;
    private UsuarioMin criadoPor;

    public EntradaHistorico(String id, String titulo, String descricao, String instante, UsuarioMin criadoPor) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        setInstante(instante);
        this.criadoPor = criadoPor;
    }

    public void setInstante(String instante) {
        if (instante == null) {
            var instant = Instant.now().with(ChronoField.NANO_OF_SECOND, 0).atZone(ZoneId.of("-3"));
            instante = instant.toString();
        }
        this.instante = instante;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class UsuarioMin {
        private String email;
        private String nome;
        private String role;
    }
}