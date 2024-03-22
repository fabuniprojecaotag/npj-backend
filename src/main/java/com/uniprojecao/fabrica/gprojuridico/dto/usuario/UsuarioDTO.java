package com.uniprojecao.fabrica.gprojuridico.dto.usuario;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioDTO {
    @Pattern(regexp = "^[0-9]{9}@projecao\\.edu\\.br|[a-z]{3,}\\.[a-z]{3,}@projecao\\.br$")
    private String email;

    @NotBlank
    @Size(min = 3)
    private String nome;

    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$")
    private String cpf;

    private String unidadeInstitucional;

    @Size(min = 6, max = 12)
    private String senha;

    @AssertTrue
    private Boolean status;

    @NotBlank
    private String role;

    public UsuarioDTO(Usuario u) {
        email = u.getEmail();
        nome = u.getNome();
        cpf = u.getCpf();
        unidadeInstitucional = u.getUnidadeInstitucional();
        senha = u.getSenha();
        status = u.getStatus();
        role = u.getRole();
    }
}
