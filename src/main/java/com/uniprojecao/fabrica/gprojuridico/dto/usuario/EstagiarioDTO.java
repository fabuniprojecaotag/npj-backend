package com.uniprojecao.fabrica.gprojuridico.dto.usuario;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EstagiarioDTO extends UsuarioDTO {
    @Pattern(regexp = "^[0-9]{9}$")
    private String matricula;

    @Pattern(regexp = "^(8|9|10)$")
    private String semestre;

    @NotNull
    private SupervisorMinDTO supervisor;

    // Este construtor é necessário por conta dos testes unitários.
    public EstagiarioDTO(String id, String email, String nome, String cpf, String u, String senha, Boolean status, String role, String matricula, String semestre, SupervisorMinDTO supervisor) {
        super(id, email, nome, cpf, u, senha, status, role);
        this.matricula = matricula;
        this.semestre = semestre;
        this.supervisor = supervisor;
    }
}
