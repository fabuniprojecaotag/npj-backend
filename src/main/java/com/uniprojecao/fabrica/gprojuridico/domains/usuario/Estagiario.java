package com.uniprojecao.fabrica.gprojuridico.domains.usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Estagiario extends Usuario {
    private String matricula;
    private String semestre;
    private String supervisor;

    // Este construtor é necessário para a classe EstagiarioAggregator funcionar no pacote de testes.
    public Estagiario(String email, String nome, String cpf, String u, String senha, Boolean status, String role, String matricula, String semestre, String supervisor) {
        super(email, nome, cpf, u, senha, status, role);
        this.matricula = matricula;
        this.semestre = semestre;
        this.supervisor = supervisor;
    }
}
