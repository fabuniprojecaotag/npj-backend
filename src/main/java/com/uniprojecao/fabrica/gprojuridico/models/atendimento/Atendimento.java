package com.uniprojecao.fabrica.gprojuridico.models.atendimento;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.uniprojecao.fabrica.gprojuridico.enums.AreaAtendimento;
import com.uniprojecao.fabrica.gprojuridico.enums.StatusAtendimento;
import com.uniprojecao.fabrica.gprojuridico.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AtendimentoCivil.class, name = "Civil"),
        @JsonSubTypes.Type(value = AtendimentoTrabalhista.class, name = "Trabalhista")
})
public abstract class Atendimento extends BaseModel {
    private String status; // Enum Status convertido em String
    private String area; // Enum Area convertido em String

    private String instante;

    private String historico;
    private Map<String, Envolvido> envolvidos = new HashMap<>();

    public void setStatus(String status) {
        this.status = StatusAtendimento.valueOf(status.replace(" ", "_").toUpperCase()).getValue(); // Ex. "Processo ativo" → "PROCESSO_ATIVO" → "Processo ativo". É necessária esta transformação para o armazenamento customizado de constantes.
    }

    public void setArea(String area) {
        this.area = AreaAtendimento.valueOf(area.toUpperCase()).getValue(); // Ex. "Trabalhista" → "TRABALHISTA" → "Trabalhista". É necessária esta transformação para o armazenamento customizado de constantes.
    }

    public void setEnvolvidos(Map<String, Envolvido> envolvidos) {
        this.envolvidos.putAll(envolvidos);
    }
}
