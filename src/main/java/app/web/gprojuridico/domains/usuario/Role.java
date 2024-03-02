package app.web.gprojuridico.domains.usuario;

import lombok.NoArgsConstructor;
import lombok.Data;

import org.springframework.security.core.GrantedAuthority;

@NoArgsConstructor
@Data
public class Role implements GrantedAuthority {

    private String authority;

    public Role(String authority) {
        this.authority = AllowedRoles.valueOf(authority).toString();
    }

    private enum AllowedRoles {
        ROLE_COORDENADOR, ROLE_ESTAGIARIO, ROLE_PROFESSOR, ROLE_SECRETARIA
    }
}
