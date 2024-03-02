package app.web.gprojuridico.domains.atendimento;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FichaCivil.class, name = "FichaCivil"),
        @JsonSubTypes.Type(value = FichaTrabalhista.class, name = "FichaTrabalhista")
})
public abstract class Ficha {

    private String assinatura;
    private Boolean dadosSensiveis;
    private List<Testemunha> testemunhas;

    public Ficha(String assinatura, Boolean dadosSensiveis, ArrayList<Testemunha> testemunhas) {
        this.assinatura = assinatura;
        this.dadosSensiveis = dadosSensiveis;
        this.testemunhas = testemunhas;
    }

    // TODO: criar métodos que adicionam e removem elementos do atributo 'testemunhas'
}
