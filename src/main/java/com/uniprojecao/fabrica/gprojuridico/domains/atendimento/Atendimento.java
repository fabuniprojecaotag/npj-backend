package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import com.uniprojecao.fabrica.gprojuridico.dto.EnvolvidoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AtendimentoCivil.class, name = "Civil"),
        @JsonSubTypes.Type(value = AtendimentoTrabalhista.class, name = "Trabalhista")
})
public abstract class Atendimento {
    @DocumentId
    private String id;

    private String status; // Enum Status convertido em String
    private String area; // Enum Area convertido em String

    @ServerTimestamp
    @JsonIgnore
    private Timestamp instante;

    private Timestamp prazoEntregaDocumentos;

    private List<EntradaHistorico> historico = new ArrayList<>();
    private Map<String, EnvolvidoDTO> envolvidos = new HashMap<>();

    public Atendimento(String id, String status, String area, Timestamp instante, Timestamp prazo, List<EntradaHistorico> historico, Map<String, EnvolvidoDTO> envolvidos) {
        this.id = id;
        setStatus(status);
        setArea(area);
        this.instante = instante;
        this.prazoEntregaDocumentos = prazo;
        setHistorico(historico);
        setEnvolvidos(envolvidos);
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status.replace(" ", "_").toUpperCase()).getValue(); // Ex. "Processo ativo" → "PROCESSO_ATIVO" → "Processo ativo". É necessária esta transformação para o armazenamento customizado de constantes.
    }

    public void setArea(String area) {
        this.area = Area.valueOf(area.toUpperCase()).getValue(); // Ex. "Trabalhista" → "TRABALHISTA" → "Trabalhista". É necessária esta transformação para o armazenamento customizado de constantes.
    }

    public void setHistorico(List<EntradaHistorico> entradas) {
        this.historico.addAll(entradas);
    }

    public void setEnvolvidos(Map<String, EnvolvidoDTO> envolvidos) {
        this.envolvidos.putAll(envolvidos);
    }

    public void removeEnvolvido(String envolvido) {
        this.envolvidos.remove(envolvido);
    }

    @Getter
    enum Status {
        REPROVADO("Reprovado"),
        ARQUIVADO("Arquivado"),
        AGUARDANDO_DOCUMENTOS("Aguardando documentos"),
        PENDENTE_DISTRIBUICAO("Pendente distribuição"),
        PROCESSO_ATIVO("Processo ativo"),
        PROCESSO_ARQUIVADO("Processo arquivado");

        private final String value;

        Status(String value) {
            this.value = value;
        }
    }

    @Getter
    enum Area {
        CIVIL("Civil"),
        CRIMINAL("Criminal"),
        FAMILIA("Família"),
        TRABALHISTA("Trabalhista");

        private final String value;

        Area(String value) {
            this.value = value;
        }
    }

    @NoArgsConstructor
    @Data
    public static class EntradaHistorico {
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
}
