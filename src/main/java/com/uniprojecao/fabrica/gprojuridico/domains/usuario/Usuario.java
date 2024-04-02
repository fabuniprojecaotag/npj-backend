package com.uniprojecao.fabrica.gprojuridico.domains.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.Exclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Estagiario.class, name = "Estagiario"),
})
public class Usuario implements UserDetails {

    @DocumentId
    private String email;
    private String nome;
    private String cpf;
    private String unidadeInstitucional;
    private String senha;
    private Boolean status;
    private String role;

    @Override
    @JsonIgnore
    @Exclude
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var coordenadorRole = new SimpleGrantedAuthority("ROLE_COORDENADOR");
        var secretariaRole = new SimpleGrantedAuthority("ROLE_SECRETARIA");
        var professorRole = new SimpleGrantedAuthority("ROLE_PROFESSOR");
        var estagiarioRole = new SimpleGrantedAuthority("ROLE_ESTAGIARIO");
        
        return switch (role) {
            case "COORDENADOR" -> List.of(estagiarioRole, professorRole, secretariaRole, coordenadorRole);
            case "SECRETARIA" -> List.of(estagiarioRole, professorRole, secretariaRole);
            case "PROFESSOR" -> List.of(estagiarioRole, professorRole);
            case "ESTAGIARIO" -> List.of(estagiarioRole);
            default -> throw new IllegalStateException("Unexpected role: " + role);
        };
    }

    @Override
    @JsonIgnore
    @Exclude
    public String getPassword() {
        return senha;
    }

    @Override
    @JsonIgnore
    @Exclude
    public String getUsername() {
        return nome;
    }

    @Override
    @JsonIgnore
    @Exclude
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    @Exclude
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    @Exclude
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    @Exclude
    public boolean isEnabled() {
        return true;
    }
}
