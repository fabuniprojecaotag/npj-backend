package com.uniprojecao.fabrica.gprojuridico.domains.usuario;

import com.uniprojecao.fabrica.gprojuridico.dto.usuario.SupervisorMinDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Estagiario extends Usuario {
    private String matricula;
    private String semestre;
    private SupervisorMin supervisor;

    // Este construtor é necessário para a classe EstagiarioAggregator funcionar no pacote de testes.
    public Estagiario(String id, String email, String nome, String cpf, String u, String senha, Boolean status, String role, String matricula, String semestre, SupervisorMin supervisor) {
        super(id, email, nome, cpf, u, senha, status, role);
        this.matricula = matricula;
        this.semestre = semestre;
        this.supervisor = supervisor;
    }

    public Estagiario(Usuario u) {
        super(u.getId(), u.getEmail(), u.getNome(), u.getCpf(), u.getUnidadeInstitucional(), u.getSenha(), u.getStatus(), u.getRole());
    }
}
