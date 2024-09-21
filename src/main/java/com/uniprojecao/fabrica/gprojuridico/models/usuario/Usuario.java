package com.uniprojecao.fabrica.gprojuridico.models.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.Exclude;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Estagiario.class, name = "Estagiario"),
})
public class Usuario implements UserDetails {
    @DocumentId
    private String id;

    @Pattern(regexp = "^(\\d{9}@projecao\\.edu\\.br|[a-z]{3,}\\.[a-z]{3,}@projecao\\.br)$")
    private String email;

    @NotBlank
    @Size(min = 3, max = 60)
    private String nome;

    @Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$")
    private String cpf;

    private String unidadeInstitucional;

    @Size(min = 6, max = 20)
    private String senha;

    @AssertTrue
    private Boolean status;

    @NotBlank
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
        return status;
    }
}
