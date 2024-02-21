package app.web.gprojuridico.model;

import app.web.gprojuridico.model.enums.Escolaridade;
import app.web.gprojuridico.model.enums.EstadoCivil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssistidoFull {

    private String nome;
    private String rg;
    private String cpf;
    private String nacionalidade;
    private String escolaridade;
    private String estadoCivil;
    private String profissao;
    private String telefone;
    private String email;
    private Filiacao filiacao;
    private String remuneracao;
    private Endereco endereco;

    // dados exclusivos da ficha civil
    private String naturalidade;
    private String dataNascimento;
    private Integer dependentes;

    // dados exclusivos da ficha trabalhista
    private Ctps ctps;
    private String pis;
    private Boolean empregadoAtualmente;

}
