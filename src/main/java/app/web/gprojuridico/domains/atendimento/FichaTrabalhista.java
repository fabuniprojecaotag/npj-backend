package app.web.gprojuridico.domains.atendimento;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class FichaTrabalhista extends Ficha {

    private String outrasInformacoes;
    private Reclamado reclamado;
    private RelacaoEmpregaticia relacaoEmpregaticia;
    private DocumentosDepositadosNpj documentosDepositadosNpj;

    public FichaTrabalhista(
            String assinatura,
            Boolean dadosSensiveis,
            String medidaJudicial,
            ArrayList<Testemunha> testemunhas,
            Reclamado reclamado,
            RelacaoEmpregaticia relacaoEmpregaticia,
            DocumentosDepositadosNpj documentosDepositadosNpj
    ) {
        super(assinatura, dadosSensiveis, testemunhas);
        this.outrasInformacoes = medidaJudicial;
        this.reclamado = reclamado;
        this.relacaoEmpregaticia = relacaoEmpregaticia;
        this.documentosDepositadosNpj = documentosDepositadosNpj;
    }
}
