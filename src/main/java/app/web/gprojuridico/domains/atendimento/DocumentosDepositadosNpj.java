package app.web.gprojuridico.domains.atendimento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentosDepositadosNpj {

    private boolean procuracao;
    private boolean declaracaoPobreza;
    private boolean ctps;
    private boolean identidade;
    private boolean cpf;
    private boolean pis;
    private boolean contrachequeUltimos3Meses;
    private boolean extratoAnaliticoContaFgts;
    private boolean trct;
    private boolean comprovanteRecAntecip13;
    private boolean acordoColetivoTrabalho;
    private String outrosDocumentos;
}
