package com.uniprojecao.fabrica.gprojuridico.domains.assistido;

import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class AssistidoTrabalhista extends Assistido {
    private Ctps ctps;
    private String pis;
    private Boolean empregadoAtualmente;

    public AssistidoTrabalhista(String nome, String rg, String cpf, String nacionalidade, String escolaridade, String estadoCivil, String profissao, String telefone, String email, Filiacao filiacao, String remuneracao, Map<String, Endereco> endereco, Ctps ctps, String pis, Boolean empregadoAtualmente) {
        super(nome, rg, cpf, nacionalidade, escolaridade, estadoCivil, profissao, telefone, email, filiacao, remuneracao, endereco);
        this.ctps = ctps;
        this.pis = pis;
        this.empregadoAtualmente = empregadoAtualmente;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Ctps {
        private String numero;
        private String serie;
        private String uf;
    }
}
