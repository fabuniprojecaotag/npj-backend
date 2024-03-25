package com.uniprojecao.fabrica.gprojuridico.dto.atendimento;

import com.google.cloud.Timestamp;
import com.uniprojecao.fabrica.gprojuridico.dto.EnvolvidoDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
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
public class AtendimentoDTO {
    // TODO: Tornar regex 'opcional' ao adicionar quantificador de 0 ou 1.
    // @Pattern(regexp = "^ATE\\d{5,}$") // exemplo[]: ["ATE00032", "ATE1234567"]
    // @Nullable
    private String id;

    private String status; // Enum Status convertido em String
    private String area; // Enum Area convertido em String

    @Nullable
    private Timestamp instante;

    private FichaDTO ficha;

    private List<EntradaHistoricoDTO> historico = new ArrayList<>();
    private Map<String, EnvolvidoDTO> envolvidos = new HashMap<>();

    public AtendimentoDTO(@Nullable String id, String status, String area, @Nullable Timestamp instante) {
        this.id = id;
        setStatus(status);
        setArea(area);
        this.instante = instante;
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status).getValue();
    }

    public void setArea(String area) {
        this.area = Area.valueOf(area).getValue();
    }

    public void setHistorico(List<EntradaHistoricoDTO> entradas) {
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
    @AllArgsConstructor
    @Data
    public static class EntradaHistoricoDTO {
        @Nullable
        private String id;

        @NotBlank
        private String titulo;
        private String descricao;

        @Nullable
        private String instante;

        private UsuarioMinDTO criadoPor;

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
        public static class UsuarioMinDTO {
            private String email;
            private String nome;
            private String role;
        }
    }
}
