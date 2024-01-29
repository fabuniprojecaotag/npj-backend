package app.web.gprojuridico.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements UserDetails {

    private int id;
    private String email;
    private String password;
    private String username; // nome
    private String matricula;
    private Perfil perfil;
    private String semestre;
    private boolean accountNonLocked; // status
    private String documentId;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Aqui você pode mapear os perfis (Perfil) para GrantedAuthority
        // Neste exemplo, assumindo que 'perfil' é uma lista de strings representando os papéis

        Set<GrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + perfil.getNome()));

        // Adicione mais autoridades conforme necessário
        // authorities.add(new SimpleGrantedAuthority("ROLE_OUTRO_PERFIL"));

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
