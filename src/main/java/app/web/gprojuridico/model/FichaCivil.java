package app.web.gprojuridico.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class FichaCivil extends Ficha {

    private String medidaJudicial;
    private ParteContraria parteContraria;

    public FichaCivil(String assinatura, Boolean dadosSensiveis, String medidaJudicial, ArrayList<Testemunha> testemunhas, ParteContraria parteContraria) {
        super(assinatura, dadosSensiveis, testemunhas);
        this.medidaJudicial = medidaJudicial;
        this.parteContraria = parteContraria;
    }
}
