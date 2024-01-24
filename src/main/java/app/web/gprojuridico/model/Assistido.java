package app.web.gprojuridico.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Assistido {
    private String nome;
    private String email;
    private String cpf;
    private String rg;
    private String naturalidade;
    private String nacionalidade;
    private String dataNascimento;
    private String estadoCivil;
    private String telefone;
    private String cidade;
    private String cep;
    private String enderecoResidencial;
    private String escolaridade;
    private String nomePai;
    private String nomeMae;
    private String profissao;
    private String remuneracao;
    private String cidadeComercial;
    private String enderecoComercial;
    private String numDependentes;
    private String documentId;

}

