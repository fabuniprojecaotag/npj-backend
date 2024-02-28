package app.web.gprojuridico.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Perfil implements GrantedAuthority {
    private int id;
    private String nome;
    private String documentId;

    @Override
    public String getAuthority() {
        return nome;
    }
}
