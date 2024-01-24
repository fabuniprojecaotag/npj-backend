package app.web.gprojuridico.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Perfil implements GrantedAuthority {
    private int id;
    private String nome;
    private String documentId;
    private List<Map<String, Object>> permissoes;
    @Override
    public String getAuthority() {
        return nome;
    }
}
