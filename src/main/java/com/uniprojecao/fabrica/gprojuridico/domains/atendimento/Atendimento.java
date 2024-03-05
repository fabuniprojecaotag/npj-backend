package com.uniprojecao.fabrica.gprojuridico.domains.atendimento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String historico;
    private String assistidoId;
    private Ficha ficha;

    public Atendimento(String status, String area, String prazo, String historico, String assistidoId, Ficha ficha) {
        this.status = status;
        this.area = area;
        this.prazoEntregaDocumentos = prazo;
        this.historico = historico;
        this.assistidoId = assistidoId;
        this.ficha = ficha;
    }
}
