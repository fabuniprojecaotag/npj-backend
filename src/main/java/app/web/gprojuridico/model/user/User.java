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
    private String senha;
    private String matricula;
    private Perfil perfil;
    private String nome;
    private String semestre;
    private String status;
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
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return nome;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Aqui você pode implementar a lógica para verificar se a conta do usuário está expirada
        // Exemplo: return true se a conta não estiver expirada, false caso contrário
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Implemente a lógica para verificar se a conta do usuário não está bloqueada
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Implemente a lógica para verificar se as credenciais do usuário não estão expiradas
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Implemente a lógica para verificar se o usuário está habilitado
        return true;
    }

}
