package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import com.uniprojecao.fabrica.gprojuridico.dto.EnvolvidoDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Atendimento {

    private String id;
    private String status;
    private String area;
    @ServerTimestamp
    @JsonIgnore
    private Timestamp instante;
    private String prazoEntregaDocumentos;
    private String historico; // TODO: Alterar tipo String para List<>
    private Ficha ficha;
    private Map<String, EnvolvidoDTO> envolvidos = new HashMap<>();

    public Atendimento(String status, String area, String prazo, String historico, Ficha ficha) {
        this.status = status;
        this.area = area;
        this.prazoEntregaDocumentos = prazo;
        this.historico = historico;
        this.ficha = ficha;
    }

    public Atendimento(String status, String area, String prazo, String historico, Ficha ficha, Map<String, EnvolvidoDTO> envolvidos) {
        this.status = status;
        this.area = area;
        this.prazoEntregaDocumentos = prazo;
        this.historico = historico;
        this.ficha = ficha;
        this.envolvidos.putAll(envolvidos);
    }
}
