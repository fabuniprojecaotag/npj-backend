package app.web.gprojuridico.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Estagiario extends Usuario {
    private String matricula;
    private String semestre;
    private String supervisor;

    public Estagiario(String email, String nome, String cpf, String unidadeInstitucional, String senha, Boolean status, String role, String matricula, String semestre, String emailSupervisor) {
        super(email, nome, cpf, unidadeInstitucional, senha, status, role);
        this.matricula = matricula;
        this.semestre = semestre;
        this.supervisor = emailSupervisor;
    }
}
