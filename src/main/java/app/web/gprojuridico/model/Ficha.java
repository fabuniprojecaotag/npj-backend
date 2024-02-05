package app.web.gprojuridico.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.cloud.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FichaCivil.class, name = "FichaCivilDTO"),
        @JsonSubTypes.Type(value = FichaTrabalhista.class, name = "FichaTrabalhistaDTO")
})
public abstract class Ficha {

    private String assinatura;
    private final Timestamp instante = Timestamp.now();
    private Boolean dadosSensiveis;
    private List<Testemunha> testemunhas;

    public Ficha(String assinatura, Boolean dadosSensiveis, ArrayList<Testemunha> testemunhas) {
        this.assinatura = assinatura;
        this.dadosSensiveis = dadosSensiveis;
        this.testemunhas = testemunhas;
    }
}
