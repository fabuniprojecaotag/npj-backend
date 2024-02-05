package app.web.gprojuridico.model;

import com.google.cloud.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Atendimento {

    private String id;
    private String status;
    private String area;
    private final Timestamp instante = Timestamp.now();
    private String prazoEntregaDocumentos;
    private String historico;
    private String assistidoId;
    private Ficha ficha;

    public Atendimento(String status, String area, String prazoEntregaDocumentos, String historico, String assistidoId, FichaCivil ficha) {
        this.status = status;
        this.area = area;
        this.prazoEntregaDocumentos = prazoEntregaDocumentos;
        this.historico = historico;
        this.assistidoId = assistidoId;
        this.ficha = ficha;
    }

    public Atendimento(String status, String area, String prazoEntregaDocumentos, String historico, String assistidoId, FichaTrabalhista ficha) {
        this.status = status;
        this.area = area;
        this.prazoEntregaDocumentos = prazoEntregaDocumentos;
        this.historico = historico;
        this.assistidoId = assistidoId;
        this.ficha = ficha;
    }
}
