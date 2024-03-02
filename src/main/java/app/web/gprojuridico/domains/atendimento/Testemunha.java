package app.web.gprojuridico.domains.atendimento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Testemunha {

    private String nome;
    private String qualificao;
    private String endereco;
}
