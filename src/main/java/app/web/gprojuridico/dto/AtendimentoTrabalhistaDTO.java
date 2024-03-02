package app.web.gprojuridico.dto;

import app.web.gprojuridico.domains.atendimento.FichaTrabalhista;
import com.google.cloud.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtendimentoTrabalhistaDTO {

    private String id;
    private String status;
    private String area;
    private Timestamp instante;
    private String prazoEntregaDocumentos;
    private String historico;
    private String assistidoId;
    private FichaTrabalhista ficha;
}
